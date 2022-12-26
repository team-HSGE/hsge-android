package com.starters.hsge.presentation.main.find

import android.Manifest
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentFindBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import net.daum.mf.map.api.*

class FindFragment : BaseFragment<FragmentFindBinding>(R.layout.fragment_find) {

    private var mCurrentLat : Double? = 0.0
    private var mCurrentLng : Double? = 0.0

    private var status : Boolean = true
    private val ACCESS_FINE_LOCATION = 1000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setZoomLevel()
        setTrackingBtn()
    }

    override fun onStop() {
        super.onStop()
        stopTracking()
    }

    private fun setZoomLevel() { binding.kakaoMapView.setZoomLevel(3, true) }

    // 내 주변 탐색 버튼 세팅
    private fun setTrackingBtn() {
        binding.trackingBtn.setOnClickListener {
            if (status == true) {
                if (checkLocationService()) {
                    // GPS가 켜져있을 경우
                    permissionCheck()

                } else {
                    // GPS가 꺼져있을 경우
                    Toast.makeText(context, "GPS를 켜주세요.", Toast.LENGTH_SHORT).show()
                }

            } else {
                stopTracking()
            }
        }
    }

    // 권한 체크
    private fun permissionCheck() {
        val isFirstCheck = prefs.getBoolean("isFirstPermissionCheck", true)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 권한 거절 (다시 한 번 물어봄)
                val builder = AlertDialog.Builder(context)
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요.")
                builder.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                }
                builder.show()
            } else {
                if (isFirstCheck) {
                    // 최초 권한 요청
                    prefs.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                } else {
                    // 다시 묻지 않음 클릭 (앱 정보 화면으로 이동)
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요.")
                    builder.setPositiveButton("설정으로 이동하기") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context?.packageName))
                        startActivity(intent)
                    }
                    builder.show()
                }
            }
        } else {
            // 권한이 있는 상태
            val lm : LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val userNowLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            mCurrentLat = userNowLocation?.latitude // 위도
            mCurrentLng = userNowLocation?.longitude // 경도

            startTracking()
        }
    }

    // 권한 요청에 따른 분기 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 요청 후 승인 됨 (추적 시작)
                Toast.makeText(context, "위치 권한이 승인되었습니다.", Toast.LENGTH_SHORT).show()
                startTracking()
            } else {
                // 권한 요청 후 거절됨 (다시 요청 or 토스트)
                Toast.makeText(context, "위치 권한이 거절되었습니다.", Toast.LENGTH_SHORT).show()
                permissionCheck()
            }
        }
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // 현재 위치 마커 세팅
    private fun setMyLocationMarker() {
        binding.kakaoMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.ic_my_location, MapPOIItem.ImageOffset(20,20))
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
                binding.kakaoMapView.removeAllCircles()
                binding.kakaoMapView.addCircle(circle1)
                binding.kakaoMapView.moveCamera(CameraUpdateFactory.newMapPoint(MapPoint.mapPointWithGeoCoord(mCurrentLat!!, mCurrentLng!!)))
            }
        }
    }

    // Tracking 상태
    private fun startTracking() {
        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        setMyLocationMarker()
        setCircle("start")

        binding.trackingBtn.setBackgroundResource(R.drawable.bg_dark_yellow_r12)
        binding.trackingBtn.text = "탐색 종료"

        binding.fabCurrentLocation.isClickable = true
        catchCurrentLocation()
        status = false
        Log.d("추적", "시작")
    }

    private fun stopTracking() {
        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        binding.kakaoMapView.setShowCurrentLocationMarker(false)
        setCircle("end")

        binding.trackingBtn.setBackgroundResource(R.drawable.bg_yellow_r12)
        binding.trackingBtn.text = "내 주변 탐색"

        binding.fabCurrentLocation.isClickable = false
        status = true
        Log.d("추적", "멈춤")
    }

    private fun catchCurrentLocation() {
        binding.fabCurrentLocation.setOnClickListener {
            setCircle("current")
        }
    }
}