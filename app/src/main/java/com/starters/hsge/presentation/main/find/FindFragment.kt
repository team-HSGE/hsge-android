package com.starters.hsge.presentation.main.find

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapView

class FindFragment : BaseFragment<FragmentFindBinding>(R.layout.fragment_find) {

    private var status = true
    private val ACCESS_FINE_LOCATION = 1000

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTrackingBtn()


    }

    private fun setMyLocationMarker() {
        binding.kakaoMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.ic_my_location, MapPOIItem.ImageOffset(20,0))
    }

    private fun setTrackingBtn() {
        binding.trackingBtn.setOnClickListener {
            if (status == true) {
                if (checkLocationService()) {
                    // GPS가 켜져있을 경우
                    permissionCheck()
                    status = false
                } else {
                    // GPS가 꺼져있을 경우
                    Toast.makeText(context, "GPS를 켜주세요.", Toast.LENGTH_SHORT).show()
                    status = true
                }

            } else {
                stopTracking()
                status = true
            }
        }
    }

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
                builder.setNegativeButton("취소") { dialog, which ->

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
                    builder.setPositiveButton("설정으로 이동") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context?.packageName))
                        startActivity(intent)
                    }
                    builder.setNegativeButton("취소") { dialog, which ->

                    }
                    builder.show()
                }
            }
        } else {
            // 권한이 있는 상태
            startTracking()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

    private fun startTracking() {
        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        setMyLocationMarker()
        Log.d("추적", "시작")
    }

    private fun stopTracking() {
        binding.kakaoMapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        Log.d("추적", "멈춤")

    }

}