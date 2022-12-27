package com.starters.hsge.presentation.main.find

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.starters.hsge.BuildConfig
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentFindBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import kotlinx.coroutines.launch
import net.daum.mf.map.api.*

class FindFragment : BaseFragment<FragmentFindBinding>(R.layout.fragment_find) {

    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    private var mCurrentLat : Double? = 0.0
    private var mCurrentLng : Double? = 0.0
    private var status : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPermissionLauncher()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()
        setZoomLevel()
        setTrackingBtn()
        catchCurrentLocation()
    }

    override fun onStop() {
        super.onStop()

        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        binding.kakaoMapView.setShowCurrentLocationMarker(false)
    }

    // 초기 권한 + GPS 상태 설정
    private fun initPermissionLauncher() {
        if (checkLocationService()){
            locationPermissionRequest =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                    if (isGranted) {
                        // 권한이 있는 상태
                        val lm : LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        mCurrentLat = userNowLocation?.latitude // 위도
                        mCurrentLng = userNowLocation?.longitude // 경도
                        setCurrentLocation()

                    } else {
                        checkPermission()
                    }
                }
        } else {
            // GPS가 꺼져있을 경우
            Toast.makeText(context, "GPS를 켜주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    // 권한 체크
    private fun checkPermission() {
        val isFirstCheck = prefs.getBoolean("isFistImgPermissionCheck", true)
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                // 권한이 있는 상태
                val lm : LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                mCurrentLat = userNowLocation?.latitude // 위도
                mCurrentLng = userNowLocation?.longitude // 경도
                setCurrentLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showFirstPermissionDialog()
            }
            else -> {
                if (isFirstCheck) {
                    prefs.edit().putBoolean("isFistImgPermissionCheck", false).apply()
                    locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                } else {
                    showSecondPermissionDialog()
                }
            }
        }
    }

    private fun showFirstPermissionDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setMessage("앱 실행을 위해서는 권한을 설정해야 합니다")
            .setPositiveButton("확인") { _, _ ->
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .show()
    }

    private fun showSecondPermissionDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setMessage("설정에서 위치 권한을 허용해주세요")
        builder.setPositiveButton("설정으로 이동하기") { _, _ ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            ).setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID))
            startActivity(intent)
        }
        builder.show()
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // 탐색 화면 들어가자 마자 수행
    private fun setCurrentLocation() {
        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        setMyLocationMarker()
    }

    // 초기 Zoom 세팅
    private fun setZoomLevel() { binding.kakaoMapView.setZoomLevel(3, true) }

    // 현재 위치 마커 세팅
    private fun setMyLocationMarker() {
        binding.kakaoMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.ic_my_location, MapPOIItem.ImageOffset(20,20))
    }

    // 내 주변 탐색 버튼 세팅
    private fun setTrackingBtn() {
        binding.trackingBtn.setOnClickListener {
            if (status) {
                // 반경 보이면서 주변 강아지 마커들 보이게 하기
                startTracking()
            } else {
                stopTracking()
            }
        }
    }

    // 현재 위치 반경 세팅
    private fun setCircle(circleName : String) {
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

        when(circleName) {
            "start" -> {
                binding.kakaoMapView.addCircle(circle1)
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))
            }
            "end" -> {
                binding.kakaoMapView.removeAllCircles()
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(mCurrentLat!!, mCurrentLng!!)))
            }
            "current" -> {
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
            setCircle("current")
        }
    }
}