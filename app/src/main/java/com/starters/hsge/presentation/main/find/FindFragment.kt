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
import android.widget.Toast
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
import com.starters.hsge.data.model.remote.response.ShakeHandResponse
import com.starters.hsge.data.service.ShakeHandService
import com.starters.hsge.databinding.FragmentFindBinding
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

    private var mp = MapPoint.mapPointWithGeoCoord(0.0, 0.0)
    private var mCurrentLat: Double = 0.0
    private var mCurrentLng: Double = 0.0

    private var uCurrentLat: Double? = 0.0
    private var uCurrentLng: Double? = 0.0
    private var uNickname: String? = null
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
    }

    override fun onResume() {
        super.onResume()

        setAutoLocation()
        if (mCurrentLat == 0.0 && mCurrentLng == 0.0) {
            binding.trackingBtn.isEnabled = false
            binding.trackingBtn.setBackgroundResource(R.drawable.bg_light_gray_r12)
            binding.fabCurrentLocation.isEnabled = false
        }
    }

    override fun onPause() {
        super.onPause()

        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        binding.kakaoMapView.setShowCurrentLocationMarker(false)
        mCurrentLat = 0.0
        mCurrentLng = 0.0
        endInfinite()
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

    // 초기 화면 위치 + 커스텀 마커 설정 (지도 중심 이동)
    private fun setCurrentLocation() {
        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        binding.kakaoMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.ic_my_location, MapPOIItem.ImageOffset(20, 20))
    }

    // 초기 Zoom 세팅
    private fun setZoomLevel() { binding.kakaoMapView.setZoomLevel(3, true) }

    // 초기 내 주변 탐색 버튼 세팅
    private fun setActiveBtn() {
        if (mCurrentLat != 0.0 && mCurrentLng != 0.0) {
            binding.trackingBtn.setBackgroundResource(R.drawable.bg_yellow_r12)
            binding.trackingBtn.isEnabled = true
            binding.fabCurrentLocation.isEnabled = true
        }
    }

    // 내 주변 탐색 버튼 세팅
    private fun setTrackingBtn() {
        binding.trackingBtn.setOnClickListener {
            if (status) {
                if (checkLocationService()) {
                    val location = CurrentLocationPostRequest(mCurrentLat.toString(), mCurrentLng.toString())
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
                startLocationUpdates()
                binding.kakaoMapView.removeAllPOIItems()
                lifecycleScope.launch(Dispatchers.IO) {
                    ShakeHandService(this@FindFragment).tryGetHandShake()
                }
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
        trackingHandler.sendEmptyMessageDelayed(0, 1000) // intervalTime만큼 반복해서 핸들러 실행
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
    private fun createMarker(uCurrentLat: Double, uCurrentLng: Double, uNickname: String) {
        val marker = MapPOIItem()
        marker.apply {
            itemName = uNickname
            mapPoint = MapPoint.mapPointWithGeoCoord(uCurrentLat, uCurrentLng)
            markerType = MapPOIItem.MarkerType.CustomImage
            customImageResourceId = R.drawable.ic_map_marker
            setCustomImageAnchor(0.5f, 0.5f)
        }
        binding.kakaoMapView.addPOIItem(marker)
    }

    // 말풍선 클릭
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {}
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {
        Toast.makeText(context, "${p1?.itemName}님에게 손을 흔들었어요!", Toast.LENGTH_SHORT).show()
    }

    // 현재 위치
    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        val mapPointGeo: GeoCoordinate = p1!!.mapPointGeoCoord
        mCurrentLat = mapPointGeo.latitude
        mCurrentLng = mapPointGeo.longitude
        mp = MapPoint.mapPointWithGeoCoord(mCurrentLat, mCurrentLng)
        binding.kakaoMapView.setMapCenterPoint(mp, true)

        setActiveBtn()
    }
    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {}
    override fun onCurrentLocationUpdateFailed(p0: MapView?) {}
    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {}

    // 서버 통신
    override fun onPostCurrentLocationSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            Log.d("PostCurrentLocation", "성공!")
        }
    }

    override fun onPostCurrentLocationFailure(message: String) {
        Log.d("PostCurrentLocation 오류", "오류: $message")
    }

    override fun onGetShakeHandSuccess(shakeHandResponse: List<ShakeHandResponse>, isSuccess: Boolean) {
        if (isSuccess) {
            for (user in shakeHandResponse) {
                uCurrentLat = user.latitude
                uCurrentLng = user.longitude
                uNickname = user.nickname
                createMarker(uCurrentLat!!, uCurrentLng!!, uNickname!!)
                Log.d("손흔들기", "사용자정보 불러와지냐?")
            }
        }
    }

    override fun onGetShakeHandFailure(message: String) {
        Log.d(" GetShakeHand 오류", "오류: $message")
    }
}