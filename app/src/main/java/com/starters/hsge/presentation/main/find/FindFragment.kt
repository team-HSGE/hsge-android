package com.starters.hsge.presentation.main.find

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
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
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
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
import net.daum.mf.map.api.*
import net.daum.mf.map.api.MapPoint.GeoCoordinate
import net.daum.mf.map.api.MapView.*
import net.daum.mf.map.n.api.internal.NativeMapLocationManager.*

class FindFragment : Fragment(), CurrentLocationEventListener, MapView.POIItemEventListener, MapView.MapViewEventListener ,ShakeHandInterface {
    lateinit var binding: FragmentFindBinding

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val trackingHandler by lazy { TrackingHandler() }

    private val dialog = FindNoticeDialogFragment()
    private var flag : Boolean = true

    private var mp = MapPoint.mapPointWithGeoCoord(0.0, 0.0)
    private var mCurrentLat: Double = 0.0
    private var mCurrentLng: Double = 0.0

    private var uCurrentLat: Double? = 0.0
    private var uCurrentLng: Double? = 0.0
    private var uNickname: String? = null
    private var uUserId: Long? = 0L

    private var waveUserId: Long? = 0L
    private var waveUserName: String? = ""

    private var myNickName: String? = ""
    private var status: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMapTilePersistentCacheEnabled(true)
        binding.kakaoMapView.setCurrentLocationRadius(0)
        binding.kakaoMapView.setMapViewEventListener(this) // 맵뷰 이벤트 리스너 등록
        binding.kakaoMapView.setCurrentLocationEventListener(this) // 현재 위치 잡는 리스너 등록
        binding.kakaoMapView.setPOIItemEventListener(this)  // 마커 클릭 이벤트 리스너 등록
        binding.kakaoMapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))  // 커스텀 말풍선 등록

        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading

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

        Log.d("되는거냐", "${mCurrentLat}, ${mCurrentLng}")
    }

    override fun onPause() {
        super.onPause()

        if (!flag) {
            dialog.dismiss()
        }

        showProgress(false)

        stopTracking()
        endInfinite()

        // 내 위치 정보 삭제
        val nickname = UsersLocationDeleteRequest(myNickName!!)
        ShakeHandService(this).tryDeleteUsersLocation(nickname)
    }

    private fun initPermissionLauncher() {
        locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }
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

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // 사용자 위치 받기
    private fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient!!.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
            override fun isCancellationRequested() = false
        }).addOnSuccessListener { location: Location? ->
            if (location != null) {
                mCurrentLat = location.latitude
                mCurrentLng = location.longitude
                Log.d("내 위치1", "${mCurrentLat}, ${mCurrentLng}")

                if (mCurrentLat != 0.0 && mCurrentLng != 0.0) {
                    showProgress(false)
                }

                mp = MapPoint.mapPointWithGeoCoord(mCurrentLat, mCurrentLng)
                binding.kakaoMapView.setMapCenterPoint(mp, true)

                setShowCurrentLocationMarker(true)
                setCurrentLocation()
            }
        }
    }

    // FAB (현재 위치로 이동)
    private fun catchCurrentLocation() {
        binding.fabCurrentLocation.setOnClickListener {
            if (checkLocationService()) {
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(mp))
            } else {
                setAutoLocation()
            }
        }
    }

    // 프로그래스바
    private fun showProgress(isShow: Boolean) {
        if(isShow) LoadingDialog.showLocationLoadingDialog(requireContext())
        else LoadingDialog.dismissLocationLoadingDialog()
    }

    // 공지 Dialog
    private fun setNoticeDialog() {
        binding.tvNotice.setOnClickListener {
            dialog.setButtonClickListener(object: FindNoticeDialogFragment.OnButtonClickListener {
                override fun onOkBtnClicked() { }
            })
            dialog.show(childFragmentManager, "CustomDialog")
            flag = false
        }
    }

    // 초기 현재위치 트래킹 + 커스텀 마커 + 줌 레벨 설정 (지도 중심 이동)
    private fun setCurrentLocation() {
        binding.kakaoMapView.setCustomCurrentLocationMarkerImage(R.drawable.ic_my_location, MapPOIItem.ImageOffset(20, 20))
        binding.kakaoMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.ic_my_location, MapPOIItem.ImageOffset(20, 20))
        binding.kakaoMapView.setZoomLevel(3, true)
    }

    // 내 주변 탐색 버튼 세팅
    private fun setTrackingBtn() {
        binding.trackingBtn.setOnClickListener {
            if (status) {
                if (checkLocationService()) {
                    // 내 위치 보내기
                    val location = CurrentLocationPostRequest(lng = mCurrentLng.toString(), lat = mCurrentLat.toString())
                    Log.d("위치 뭐냐", "${mCurrentLat}, ${mCurrentLng}")
                    ShakeHandService(this@FindFragment).tryPostCurrentLocation(location)
                    // 트래킹 시작
                    startTracking()
                } else {
                    // GPS 켜기
                    setAutoLocation()
                }
            } else {
                // 트래킹 중지
                stopTracking()
            }
        }
    }

    // 반경 세팅
    private fun setRadius() {
        binding.kakaoMapView.setCurrentLocationRadius(2000)
        binding.kakaoMapView.setCurrentLocationRadiusStrokeColor(Color.argb(30, 37, 114, 209))
        binding.kakaoMapView.setCurrentLocationRadiusFillColor(Color.argb(30, 37, 114, 209))
    }

    // 지도뷰의 중심좌표와 줌레벨을 Circle이 모두 나오도록 조정
    private fun setCameraCircle(circleName: String) {
        when (circleName) {
            "start" -> {
                binding.kakaoMapView.setCurrentLocationRadius(0)
                setRadius()
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(mp, 5F))
            }
            "end" -> {
                binding.kakaoMapView.setCurrentLocationRadius(0)
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(mp))
            }
        }
    }

    // Tracking 상태 (시작)
    private fun startTracking() {
        setCameraCircle("start")
        startInfinite()
        binding.trackingBtn.setBackgroundResource(R.drawable.bg_dark_yellow_r12)
        binding.trackingBtn.text = "탐색 종료"
        status = false
        Log.d("추적", "시작")
    }

    // Tracking 상태 (중지)
    private fun stopTracking() {
        setCameraCircle("end")
        endInfinite()
        binding.kakaoMapView.removeAllPOIItems()
        binding.trackingBtn.setBackgroundResource(R.drawable.bg_yellow_r12)
        binding.trackingBtn.text = "내 주변 탐색"
        status = true

        // 내 위치 정보 삭제
        val nickname = UsersLocationDeleteRequest(myNickName!!)
        ShakeHandService(this).tryDeleteUsersLocation(nickname)
        Log.d("추적", "멈춤")
    }

    // 일정 시간마다 손 흔들기 대상 찾기
    @SuppressLint("HandlerLeak")
    private inner class TrackingHandler: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == 0){
                val location = CurrentLocationPostRequest(lng = mCurrentLng.toString(), lat = mCurrentLat.toString())
                ShakeHandService(this@FindFragment).tryPostCurrentLocation(location)
            }
        }
    }

    private fun startInfinite() {
        trackingHandler.removeMessages(0) // 이거 안하면 핸들러가 여러개로 계속 늘어남
        trackingHandler.sendEmptyMessageDelayed(0, 500) // intervalTime만큼 반복해서 핸들러 실행
        trackingHandler.postDelayed(::startInfinite, 12000) // 한 번 돌고 지연시간
    }

    private fun endInfinite() {
        trackingHandler.removeCallbacksAndMessages(null)
    }

    // 새로운 마커 생성
    private fun createMarker(uCurrentLat: Double, uCurrentLng: Double, uNickname: String, uUserId: Long) {
        val marker = MapPOIItem()
        marker.apply {
            tag = uUserId.toInt()
            itemName = uNickname
            mapPoint = MapPoint.mapPointWithGeoCoord(uCurrentLat, uCurrentLng)
            markerType = MapPOIItem.MarkerType.CustomImage
            customImageResourceId = R.drawable.ic_map_marker
            setCustomImageAnchor(0.5f, 0.5f)
        }
        binding.kakaoMapView.addPOIItem(marker)
    }

    // 상대방 마커 클릭 리스너 (말풍선 클릭)
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {
        waveUserId = p1?.tag?.toLong()
        waveUserName = p1?.itemName
        Log.d("상대방 아이디", "${p1?.itemName} : ${p1?.tag}")

        ShakeHandService(this).tryPostHandShake(userId = waveUserId)
    }
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {}
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}

    // 현재 위치 트랙킹 리스너 메소드 (내 현재 위치 갱신)
    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        val mapPointGeo: GeoCoordinate = p1!!.mapPointGeoCoord
        mCurrentLat = mapPointGeo.latitude
        mCurrentLng = mapPointGeo.longitude
        mp = MapPoint.mapPointWithGeoCoord(mCurrentLat, mCurrentLng)
        binding.kakaoMapView.setMapCenterPoint(mp, true)
        Log.d("내 위치", "${mCurrentLat}, ${mCurrentLng}")
    }
    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {}
    override fun onCurrentLocationUpdateFailed(p0: MapView?) {}
    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {}

    // 맵뷰 이벤트 리스너 메소드 (현재 위치 마커 세팅)
    override fun onMapViewInitialized(p0: MapView?) {
        binding.kakaoMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.ic_my_location, MapPOIItem.ImageOffset(20, 20))
    }
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {}
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {}

    // 서버 통신 (내 현재위치 보내기)
    override fun onPostCurrentLocationSuccess(currentUserLocation: CurrentLocationPostResponse, isSuccess: Boolean) {
        if (isSuccess) {
            myNickName = currentUserLocation.name
            Log.d("PostCurrentLocation", "${myNickName}")

            binding.kakaoMapView.removeAllPOIItems()
            ShakeHandService(this@FindFragment).tryGetHandShake()
        }
    }

    override fun onPostCurrentLocationFailure(message: String) {
        Log.d("PostCurrentLocation 오류", "오류: $message")
    }

    // 서버 통신 (반경 내 유저 정보 불러오기)
    override fun onGetShakeHandSuccess(usersLocationNearbyGetResponse: List<UsersLocationNearbyGetResponse>, isSuccess: Boolean) {
        if (isSuccess) {
            for (user in usersLocationNearbyGetResponse) {
                uCurrentLat = user.lat
                uCurrentLng = user.lng
                uNickname = user.name
                uUserId = user.userId
                createMarker(uCurrentLng!!, uCurrentLat!!, uNickname!!, uUserId!!)  // lng -> lat, lat -> lng
                Log.d("주변 사람들 정보", "${uCurrentLat}, ${uCurrentLng}, ${uNickname}, ${uUserId}")
            }
        }
    }

    override fun onGetShakeHandFailure(message: String) {
        Log.d(" GetShakeHand 오류", "오류: $message")
    }

    // 서버 통신 (내 위치정보 삭제하기)
    override fun onDeleteUsersLocationSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            Log.d("DeleteUsersLocation", "성공!")
        }
    }

    override fun onDeleteUsersLocationFailure(message: String) {
        Log.d(" DeleteUsersLocation 오류", "오류: $message")
    }

    // 서버 통신 (손흔들기)
    override fun onPostShakeHandSuccess(isSuccess: Boolean, code: Int) {
        if (isSuccess){
            Log.d("PostShakeHand", "성공")
            requireContext().showToast("${waveUserName}님에게 손을 흔들었어요!")

        } else {
            Log.d("PostShakeHand 오류", "Error code : ${code}")
            requireContext().showToast("잠시 후 다시 시도해주세요")
        }
    }

    override fun onPostShakeHandFailure(message: String) {
        Log.d(" PostShakeHand 오류", "오류: $message")
        requireContext().showToast("잠시 후 다시 시도해주세요")
    }
}
