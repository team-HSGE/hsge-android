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
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.ShakeHandInterface
import com.starters.hsge.data.model.remote.request.CurrentLocationPostRequest
import com.starters.hsge.data.model.remote.response.ShakeHandResponse
import com.starters.hsge.data.service.ShakeHandService
import com.starters.hsge.databinding.FragmentFindBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import net.daum.mf.map.api.*

class FindFragment : BaseFragment<FragmentFindBinding>(R.layout.fragment_find), MapView.POIItemEventListener, ShakeHandInterface {

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val trackingHandler by lazy { TrackingHandler() }
    private val trackingCircle by lazy { TrackingCircle() }

    private var mCurrentLat: Double? = 0.0
    private var mCurrentLng: Double? = 0.0

    private var uCurrentLat: Double? = 0.0
    private var uCurrentLng: Double? = 0.0
    private var uNickname: String? = null
    private var status: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionLauncher()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startLocationUpdates()
        setZoomLevel()
        setTrackingBtn()
        catchCurrentLocation()

        binding.kakaoMapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))  // 커스텀 말풍선 등록
        binding.kakaoMapView.setPOIItemEventListener(this)  // 마커 클릭 이벤트 리스너 등록
    }

    override fun onResume() {
        super.onResume()
        setAutoLocation()
    }

    override fun onStop() {
        super.onStop()
        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        binding.kakaoMapView.setShowCurrentLocationMarker(false)
        endInfinite()
    }

    private fun initPermissionLauncher() {
        locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }
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
                Log.d("위도경도", "${mCurrentLat}, ${mCurrentLng}")

                val location = CurrentLocationPostRequest(mCurrentLat.toString(), mCurrentLng.toString())
                ShakeHandService(this).tryPostCurrentLocation(location)
                setCurrentLocation()
            }
        }
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

    // 초기 화면 위치 + 커스텀 마커 설정
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
                    setCircle("start")
                    startTracking()
                } else {
                    setAutoLocation()
                }

            } else {
                stopTracking()
            }
        }
    }

    // 현재 위치 반경 세팅
    private fun setCircle(circleName: String) {
        val circle1 = MapCircle(
            MapPoint.mapPointWithGeoCoord(mCurrentLat!!, mCurrentLng!!),  // center
            3000,  // radius
            Color.argb(30, 37, 114, 209),  // strokeColor
            Color.argb(30, 37, 114, 209) // fillColor
        )

        // 지도뷰의 중심좌표와 줌레벨을 Circle이 모두 나오도록 조정.
        val mapPointBoundsArray = arrayOf(circle1.bound)
        val mapPointBounds = MapPointBounds(mapPointBoundsArray)
        val padding = 50 // px

        when (circleName) {
            "start" -> {
                binding.kakaoMapView.removeAllCircles()
                binding.kakaoMapView.addCircle(circle1)
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))
            }
            "tracking" -> {
                binding.kakaoMapView.removeAllCircles()
                binding.kakaoMapView.addCircle(circle1)
            }
            "end" -> {
                binding.kakaoMapView.removeAllCircles()
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(mCurrentLat!!, mCurrentLng!!)))
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
        setCircle("end")
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
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(mCurrentLat!!, mCurrentLng!!)))
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
                ShakeHandService(this@FindFragment).tryGetHandShake()
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private inner class TrackingCircle: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == 0){ setCircle("tracking") }
        }
    }

    private fun startInfinite() {
        trackingHandler.removeMessages(0) // 이거 안하면 핸들러가 여러개로 계속 늘어남
        trackingHandler.sendEmptyMessageDelayed(0, 3000) // intervalTime만큼 반복해서 핸들러 실행
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

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {}
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {
        Toast.makeText(context, "${p1?.itemName}님에게 손을 흔들었어요!", Toast.LENGTH_SHORT).show()
    }

    // 서버 통신
    override fun onPostCurrentLocationSuccess(isSuccess: Boolean) {
        if (isSuccess) { Log.d("PostCurrentLocation", "성공!") }
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
            }
        }
    }

    override fun onGetShakeHandFailure(message: String) {
        Log.d(" GetShakeHand 오류", "오류: $message")
    }
}