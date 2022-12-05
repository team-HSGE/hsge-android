package com.starters.hsge.presentation.register.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserLocationBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity

class UserLocationFragment :
    BaseFragment<FragmentUserLocationBinding>(R.layout.fragment_user_location) {

    private var fusedLocationClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    private val REQUEST_PERMISSION_LOCATION = 10

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeDoneButton()

        if (!prefs.getString("address", null).isNullOrEmpty()) {
            binding.tvMyLocation.text = prefs.getString("address", "0")
            changeDoneButton()
        }

        initListener()
        setNavigation()
    }

    private fun changeDoneButton(){
        binding.btnNext.isEnabled = !binding.tvMyLocation.text.isNullOrEmpty()
    }


    private fun initListener() {
        // 시스템 권한 대화상자 요청
        binding.btnSearch.setOnClickListener {
            if (isNetworkAvailable(requireContext()) && isEnableLocationSystem(requireContext())) {
                if (checkPermissionForLocation(requireContext())) {
                    startLocationUpdates()
                    showLoadingDialog(requireContext())

                }
            } else if (!isNetworkAvailable(requireContext())) { // 네트워크 연결 안 됨
                binding.tvMyLocation.text = null
                changeDoneButton()

                Toast.makeText(requireContext(), "네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show()
            } else if (!isEnableLocationSystem(requireContext())) { // 위치 안 켜져 있음
                binding.tvMyLocation.text = null
                changeDoneButton()
                Toast.makeText(requireContext(), "위치를 켜주세요.", Toast.LENGTH_SHORT).show()
            } else {
                binding.tvMyLocation.text = null
                changeDoneButton()
                Toast.makeText(requireContext(), "에러 발생", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNext.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)

            // 다음 누르면 이 test_latitude를 멀티파트에 담아서 통신으로 보내면 됨
            val latitude = prefs.getString("latitude", "0").toString().toDouble()
            val longitude = prefs.getString("longitude", "0").toString().toDouble()
            //Log.d("테스트_위도경도", "위도:${latitude}, 경도:${longitude} ")

            prefs.edit().remove("address").apply()
            prefs.edit().remove("longitude").apply()
            prefs.edit().remove("latitude").apply()
            activity?.finish() //RegisterActivity 종료

            //Navigation.findNavController(binding.root).navigate(R.id.action_userLocationFragment_to_userDistanceFragment)
        }
    }

    // 네트워크 상태 체크
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkStatus: NetworkInfo? = connectManager.activeNetworkInfo
        return networkStatus?.isConnectedOrConnecting == true
    }


    // 기기 위치 정보 켜져있는지 여부 확인
    private fun isEnableLocationSystem(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            locationManager!!.isLocationEnabled
        } else {
            val mode = Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }


    // 권한 체크
    private fun checkPermissionForLocation(context: Context): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.tvMyLocation.text = null // 권한 확인 할 때 힌트만 보이는 상태로

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) { // 위치 권한 허용되어있는 경우
                changeDoneButton()
                true
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){ // 위치 권한 거부되어있는 경우
                // 권한이 없으므로 권한 요청 알림 보내기
                changeDoneButton()
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )

                false
            }
            else{ // 위치 권한 거부 및 다시 묻지 않음인 경우
                Toast.makeText(requireContext(), "위치 권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
                changeDoneButton()
                false
            }
        } else {
            true
        }
    }


    // 위치 갱신
    private fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        fusedLocationClient!!.getCurrentLocation(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT).show()
                else {

                    prefs.edit().putString("latitude", location.latitude.toString()).apply()
                    prefs.edit().putString("longitude", location.longitude.toString()).apply()

                    val geocoder = Geocoder(requireContext())
                    convertToAddress(geocoder, location)
                }
            }
    }


    // 위경도 -> geocoder
    private fun convertToAddress(geocoder: Geocoder, it: Location) {
        val address = geocoder.getFromLocation(it.latitude, it.longitude, 1)

        val addressLine = address?.get(0)?.getAddressLine(0)
        val addressList = addressLine?.split(" ") as ArrayList<String>
        addressList.removeAt(0)
        addressList.removeAt(addressList.size - 1)

        val locationAddress = StringBuilder()
        for (i in addressList) {
            locationAddress.append(i)
            locationAddress.append(" ")
        }
        binding.tvMyLocation.text = locationAddress
        prefs.edit().putString("address", locationAddress.toString()).apply()

        dismissLoadingDialog()
        changeDoneButton()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}