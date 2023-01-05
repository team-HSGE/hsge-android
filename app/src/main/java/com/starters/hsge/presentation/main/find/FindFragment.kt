package com.starters.hsge.presentation.main.find

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.starters.hsge.App.Companion.prefs
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.ShakeHandInterface
import com.starters.hsge.data.model.remote.request.CurrentLocationPostRequest
import com.starters.hsge.data.model.remote.request.UsersLocationDeleteRequest
import com.starters.hsge.data.model.remote.response.CurrentLocationPostResponse
import com.starters.hsge.data.model.remote.response.UsersLocationNearbyGetResponse
import com.starters.hsge.data.service.ShakeHandService
import com.starters.hsge.databinding.FragmentFindBinding
import com.starters.hsge.presentation.common.extension.showToast
import com.starters.hsge.presentation.common.util.LoadingDialog
import com.starters.hsge.presentation.dialog.FindNoticeDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.daum.mf.map.api.*
import net.daum.mf.map.api.MapPoint.GeoCoordinate
import net.daum.mf.map.api.MapView.CurrentLocationEventListener
import net.daum.mf.map.api.MapView.setMapTilePersistentCacheEnabled
import net.daum.mf.map.n.api.internal.NativeMapLocationManager.*

class FindFragment : Fragment(), CurrentLocationEventListener, MapView.POIItemEventListener, ShakeHandInterface {
    lateinit var binding: FragmentFindBinding

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private val trackingHandler by lazy { TrackingHandler() }
    private val trackingCircle by lazy { TrackingCircle() }

    private val dialog = FindNoticeDialogFragment()

    private var mp = MapPoint.mapPointWithGeoCoord(0.0, 0.0)
    private var mCurrentLat: Double = 0.0
    private var mCurrentLng: Double = 0.0

    private var uCurrentLat: Double? = 0.0
    private var uCurrentLng: Double? = 0.0
    private var uNickname: String? = null
    private var uUserId: Long? = 0L

    private var myNickName: String? = ""
    private var userId: Long? = 0L
    private var status: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionLauncher()
        setMapTilePersistentCacheEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kakaoMapView.setCurrentLocationRadius(0)
        binding.kakaoMapView.setCurrentLocationEventListener(this) // 현재 위치 잡는 리스너 등록
        binding.kakaoMapView.setPOIItemEventListener(this)  // 마커 클릭 이벤트 리스너 등록
        binding.kakaoMapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))  // 커스텀 말풍선 등록

        startLocationUpdates()
        setZoomLevel()
        setTrackingBtn()
        catchCurrentLocation()
        setNoticeDialog()
    }

    override fun onResume() {
        super.onResume()

        setAutoLocation()
        binding.kakaoMapView.setCurrentLocationRadius(0)

        if (mCurrentLat == 0.0 && mCurrentLng == 0.0) {
            showProgress(true)
        }
    }

    override fun onPause() {
        super.onPause()

        dialog.dismiss()
        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        binding.kakaoMapView.setShowCurrentLocationMarker(false)
        mCurrentLat = 0.0
        mCurrentLng = 0.0
        stopTracking()
        endInfinite()

        // 내 위치 정보 삭제
        var nickname = UsersLocationDeleteRequest(myNickName!!)
        ShakeHandService(this).tryDeleteUsersLocation(nickname)

        if (mCurrentLat == 0.0 && mCurrentLng == 0.0) {
            showProgress(false)
        }
    }

    private fun initPermissionLauncher() {
        locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }
    }

    // 사용자 위치 받기
    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        setCurrentLocation()
    }

    // 권한 체크
    private fun checkPermissionForLocation() {
        val isFirstCheck = prefs.getBoolean("isFistLocationPermissionCheck", true)
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showFirstPermissionDialog()
            }

            else -> {
                if (isFirstCheck) {
                    prefs.edit().putBoolean("isFistLocationPermissionCheck", false).apply()
                    locationPermissionRequest.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                } else {
                    showSecondPermissionDialog()
                }
            }
        }
    }

    private fun showFirstPermissionDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("앱 실행을 위해서는 권한을 설정해야 합니다")
            .setPositiveButton("확인") { _, _ ->
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }.show()
    }

    private fun showSecondPermissionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("설정에서 권한을 허용해주세요")
        builder.setPositiveButton("설정으로 이동하기") { _, _ ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                    "package", requireContext().packageName, null
                )
            )
            startActivity(intent)
        }
        builder.show()
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // 프로그래스바
    private fun showProgress(isShow: Boolean) {
        if(isShow) LoadingDialog.showLocationLoadingDialog(requireContext())
        else LoadingDialog.dismissLocationLoadingDialog()

    }

    // 공지 다이얼로그
    private fun setNoticeDialog() {
        binding.tvNotice.setOnClickListener {
            dialog.setButtonClickListener(object: FindNoticeDialogFragment.OnButtonClickListener {
                override fun onOkBtnClicked() {
                }
            })
            dialog.show(childFragmentManager, "CustomDialog")
        }
    }

    // 초기 화면 위치 + 커스텀 마커 설정 (지도 중심 이동)
    private fun setCurrentLocation() {
        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        binding.kakaoMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.ic_my_location, MapPOIItem.ImageOffset(20, 20))
    }

    // 초기 Zoom 세팅
    private fun setZoomLevel() { binding.kakaoMapView.setZoomLevel(3, true) }

    // 내 주변 탐색 버튼 세팅
    private fun setTrackingBtn() {
        binding.trackingBtn.setOnClickListener {
            if (status) {
                if (checkLocationService()) {
                    val location = CurrentLocationPostRequest(lng = mCurrentLng.toString(), lat = mCurrentLat.toString())
                    lifecycleScope.launch(Dispatchers.IO) {
                        ShakeHandService(this@FindFragment).tryPostCurrentLocation(location)
                    }
                    setCameraCircle("start")
                    startTracking()

                } else {
                    setAutoLocation()
                }

            } else {
                stopTracking()
            }
        }
    }

    // 반경 세팅
    private fun addCircle() {
        binding.kakaoMapView.setCurrentLocationRadius(2000)
        binding.kakaoMapView.setCurrentLocationRadiusStrokeColor(Color.argb(30, 37, 114, 209))
        binding.kakaoMapView.setCurrentLocationRadiusFillColor(Color.argb(30, 37, 114, 209))
    }

    // 지도뷰의 중심좌표와 줌레벨을 Circle이 모두 나오도록 조정
    private fun setCameraCircle(circleName: String) {
        val circle1 = MapCircle(mp, 3000, 0,0)
        val mapPointBoundsArray = arrayOf(circle1.bound)
        val mapPointBounds = MapPointBounds(mapPointBoundsArray)
        val padding = 50 // px

        when (circleName) {
            "start" -> {
                binding.kakaoMapView.setCurrentLocationRadius(0)
                addCircle()
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))
            }
            "tracking" -> {
                binding.kakaoMapView.setCurrentLocationRadius(0)
                addCircle()
                Log.d("손흔들기", "반원나오냐?")
            }
            "end" -> {
                binding.kakaoMapView.setCurrentLocationRadius(0)
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(mp))
            }
        }
    }

    // Tracking 상태
    private fun startTracking() {
        startInfinite()
        startCircle()
        binding.trackingBtn.setBackgroundResource(R.drawable.bg_dark_yellow_r12)
        binding.trackingBtn.text = "탐색 종료"
        status = false
        Log.d("추적", "시작")
    }

    private fun stopTracking() {
        setCameraCircle("end")
        endInfinite()
        binding.kakaoMapView.removeAllPOIItems()
        binding.trackingBtn.setBackgroundResource(R.drawable.bg_yellow_r12)
        binding.trackingBtn.text = "내 주변 탐색"

        // 내 위치 정보 삭제
        var nickname = UsersLocationDeleteRequest(myNickName!!)
        ShakeHandService(this).tryDeleteUsersLocation(nickname)
        status = true
        Log.d("추적", "멈춤")
    }

    // 현재 위치로 이동 (FAB)
    private fun catchCurrentLocation() {
        binding.fabCurrentLocation.setOnClickListener {
            if (checkLocationService()) {
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(mp))
            } else {
                setAutoLocation()
            }
        }
    }

    // GPS 꺼져 있을 때 자동으로 설정
    private fun setAutoLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500).apply {
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            // GPS가 꺼져있을 경우
            binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
            binding.kakaoMapView.setShowCurrentLocationMarker(false)
            binding.trackingBtn.setBackgroundResource(R.drawable.bg_light_gray_r12)

            if (exception is ResolvableApiException) {
                Log.d("locationRequest", "OnFailure")
                try {
                    exception.startResolutionForResult(requireActivity(), 100)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d("locationRequest", sendEx.message.toString())
                }
            }
        }.addOnSuccessListener {
            checkPermissionForLocation()
        }
    }

    // 일정 시간마다 손 흔들기 대상 찾기
    @SuppressLint("HandlerLeak")
    private inner class TrackingHandler: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == 0){
                val location = CurrentLocationPostRequest(lng = mCurrentLng.toString(), lat = mCurrentLat.toString())
                ShakeHandService(this@FindFragment).tryPostCurrentLocation(location)
                binding.kakaoMapView.removeAllPOIItems()
                ShakeHandService(this@FindFragment).tryGetHandShake()
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private inner class TrackingCircle: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == 0){ setCameraCircle("tracking") }
        }
    }

    private fun startInfinite() {
        trackingHandler.removeMessages(0) // 이거 안하면 핸들러가 여러개로 계속 늘어남
        trackingHandler.sendEmptyMessageDelayed(0, 500) // intervalTime만큼 반복해서 핸들러 실행
        trackingHandler.postDelayed(::startInfinite, 15000) // 한 번 돌고 지연시간
    }

    private fun startCircle() {
        trackingCircle.removeMessages(0)
        trackingCircle.sendEmptyMessageDelayed(0, 500)
        trackingCircle.postDelayed(::startCircle, 500)
    }

    private fun endInfinite() {
        trackingHandler.removeCallbacksAndMessages(null)
        trackingCircle.removeCallbacksAndMessages(null)
    }

    // 새로운 마커 생성
    private fun createMarker(uCurrentLat: Double, uCurrentLng: Double, uNickname: String, uUserId: Long) {
        val marker = MapPOIItem()
        marker.apply {
            itemName = uNickname
            mapPoint = MapPoint.mapPointWithGeoCoord(uCurrentLat, uCurrentLng)
            Log.d("다른 유저 위경도", "${uCurrentLat}, ${uCurrentLng}")
            markerType = MapPOIItem.MarkerType.CustomImage
            customImageResourceId = R.drawable.ic_map_marker
            setCustomImageAnchor(0.5f, 0.5f)
        }
        userId = uUserId
        binding.kakaoMapView.addPOIItem(marker)
    }

    // 말풍선 클릭
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {}
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {
        // TODO: userId 넘겨야 함
        ShakeHandService(this).tryPostHandShake(22)
        requireContext().showToast("${p1?.itemName}님에게 손을 흔들었어요!")
    }

    // 내 현재 위치 갱신
    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        val mapPointGeo: GeoCoordinate = p1!!.mapPointGeoCoord
        mCurrentLat = mapPointGeo.latitude
        mCurrentLng = mapPointGeo.longitude
        Log.d("내 위치", "${mCurrentLat}, ${mCurrentLng}")
        mp = MapPoint.mapPointWithGeoCoord(mCurrentLat, mCurrentLng)
        binding.kakaoMapView.setMapCenterPoint(mp, true)

        if (mCurrentLat != 0.0 && mCurrentLng != 0.0) {
            showProgress(false)
        }
    }
    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {}
    override fun onCurrentLocationUpdateFailed(p0: MapView?) {}
    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {}

    // 서버 통신
    override fun onPostCurrentLocationSuccess(currentUserLocation: CurrentLocationPostResponse, isSuccess: Boolean) {
        if (isSuccess) {
            myNickName = currentUserLocation.name
            Log.d("PostCurrentLocation", "${myNickName}")
        }
    }

    override fun onPostCurrentLocationFailure(message: String) {
        Log.d("PostCurrentLocation 오류", "오류: $message")
    }

    override fun onGetShakeHandSuccess(usersLocationNearbyGetResponse: List<UsersLocationNearbyGetResponse>, isSuccess: Boolean) {
        if (isSuccess) {
            for (user in usersLocationNearbyGetResponse) {
                uCurrentLat = user.lat
                uCurrentLng = user.lng
                uNickname = user.name
                uUserId = user.userId
                Log.d("주변 사람들 정보", "${uCurrentLat}, ${uCurrentLng}, ${uNickname}, ${uUserId}")

                // lng -> lat, lat -> lng
                createMarker(uCurrentLng!!, uCurrentLat!!, uNickname!!, uUserId!!)
            }
        }
    }

    override fun onGetShakeHandFailure(message: String) {
        Log.d(" GetShakeHand 오류", "오류: $message")
    }

    override fun onDeleteUsersLocationSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            Log.d("DeleteUsersLocation", "성공!")
        }
    }

    override fun onDeleteUsersLocationFailure(message: String) {
        Log.d(" DeleteUsersLocation 오류", "오류: $message")
    }

    override fun onPostShakeHandSuccess(isSuccess: Boolean, code: Int) {
        if (isSuccess){
            Log.d(" PostShakeHand", "성공")
        } else {
            Log.d(" PostShakeHand 오류", "Error code : ${code}")
            requireContext().showToast("잠시 후 다시 시도해주세요")
        }
    }

    override fun onPostShakeHandFailure(message: String) {
        Log.d(" PostShakeHand 오류", "오류: $message")
        requireContext().showToast("잠시 후 다시 시도해주세요")
    }
}