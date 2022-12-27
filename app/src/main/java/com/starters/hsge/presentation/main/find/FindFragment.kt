package com.starters.hsge.presentation.main.find

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentFindBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import kotlinx.coroutines.launch
import net.daum.mf.map.api.*

class FindFragment : BaseFragment<FragmentFindBinding>(R.layout.fragment_find) {

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var mCurrentLat: Double? = 0.0
    private var mCurrentLng: Double? = 0.0
    private var status: Boolean = true
    private val TAG = "SOL_LOG"

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
    }

    override fun onResume() {
        super.onResume()
        setAutoLocation()

    }

    override fun onStop() {
        super.onStop()
        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        binding.kakaoMapView.setShowCurrentLocationMarker(false)
    }

    // 권한 런처
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
            }
            .show()
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
    private fun setZoomLevel() {
        binding.kakaoMapView.setZoomLevel(3, true)
    }

    // 내 주변 탐색 버튼 세팅
    private fun setTrackingBtn() {
        binding.trackingBtn.setOnClickListener {
            if (status) {
                if (checkLocationService()) {
                    // 반경 보이면서 주변 강아지 마커들 보이게 하기
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
                binding.kakaoMapView.addCircle(circle1)
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))
            }
            "end" -> {
                binding.kakaoMapView.removeAllCircles()
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(mCurrentLat!!, mCurrentLng!!)))
            }
        }
    }

    // Tracking 상태
    private fun startTracking() {
        setCircle("start")
        binding.trackingBtn.setBackgroundResource(R.drawable.bg_dark_yellow_r12)
        binding.trackingBtn.text = "탐색 종료"
        status = false
        Log.d("추적", "시작")
    }

    private fun stopTracking() {
        setCircle("end")
        binding.trackingBtn.setBackgroundResource(R.drawable.bg_yellow_r12)
        binding.trackingBtn.text = "내 주변 탐색"
        status = true
        Log.d("추적", "멈춤")
    }

    private fun catchCurrentLocation() {
        binding.fabCurrentLocation.setOnClickListener {
            if (checkLocationService()) {
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(mCurrentLat!!, mCurrentLng!!)))
            } else {
                setAutoLocation()
            }
        }
    }

    private fun setAutoLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000).apply {
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
                Log.d(TAG, "OnFailure")
                try {
                    exception.startResolutionForResult(requireActivity(), 100)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, sendEx.message.toString())
                }
            }
        }

        task.addOnSuccessListener {
            checkPermissionForLocation()
        }
    }
}
