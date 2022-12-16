package com.starters.hsge.presentation.register.fragment.userLocation

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.starters.hsge.R
import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.databinding.FragmentUserLocationBinding
import com.starters.hsge.domain.UriUtil
import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.home.network.RetrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserLocationFragment :
    BaseFragment<FragmentUserLocationBinding>(R.layout.fragment_user_location) {

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeDoneButton()

        // 데이터 유지

        if (prefs.getInt("getLocationFrom", 0) == 1) { // 위치 새로고침 하다가 나간 경우
            lifecycleScope.launch {
                // TODO : 마이페이지에서 받은 정보 띄우기, 번들로 넘어온 town, location, longitude 값 할당하기
                // 받아와서 수정
                registerViewModel.saveUserLocation("테스트 장소 : 마이페이지에서 원래 장소가 넘어왓다고 칩시다").apply { }
                registerViewModel.saveUserLatitude(37.0).apply { }
                registerViewModel.saveUserLongitude(126.0).apply { }

                binding.tvMyLocation.text = registerViewModel.fetchUserLocation().first()
                changeDoneButton()
            }

        } else { // 회원가입하다가 앱 종료인 경우
            lifecycleScope.launch {
                if (!registerViewModel.fetchUserLocation().first().isNullOrEmpty()) {
                    binding.tvMyLocation.text = registerViewModel.fetchUserLocation().first()
                    changeDoneButton()
                }
            }
        }

        initPermissionLauncher()
        initListener()
        setNavigation()

    }



    private fun initPermissionLauncher() {
        locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}
    }

    private fun changeDoneButton() {
        binding.btnNext.isEnabled = !binding.tvMyLocation.text.isNullOrEmpty()
    }


    private fun initListener() {

        // 사용자 위치 정보 받기
        binding.btnSearch.setOnClickListener {
            // GPS check & checkPermission
            if (isEnableLocationSystem(requireContext())) {
                checkPermissionForLocation()
            } else {
                Toast.makeText(context, "위치를 켜주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNext.setOnClickListener {

            // 홈 화면으로 이동

            if (prefs.getInt("getLocationFrom", 0) == 1) {
                Log.d("from?", "mypage")
                // 위도, 경도, 장소 보내는 post api 통신

                lifecycleScope.launch {
                    val latitude = registerViewModel.fetchUserLatitude().first()
                    val longitude = registerViewModel.fetchUserLongitude().first()
                    val town = registerViewModel.fetchUserLocation().first()

                    putLocationRetrofitWork(latitude, longitude, town)

                    registerViewModel.deleteAllInfo()

                    (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE

                    prefs.edit().remove("getLocationFrom").apply()

                }
            } else {
                Log.d("from?", "register")

                lifecycleScope.launch {

                    // 저장된 이미지 타입 변환: String -> Uri -> File
                    val imgUri = registerViewModel.fetchDogPhoto().first().toUri()
                    val imgFile = UriUtil.toFile(requireContext(), imgUri)

                    val registerInfo = RegisterInfo(
                        email = prefs.getString("email", ""),
                        userNickName = registerViewModel.fetchUserNickname().first(),
                        userIcon = prefs.getInt("resId", 0),
                        dogName = registerViewModel.fetchDogName().first(),
                        dogAge = registerViewModel.fetchDogAge().first(),
                        dogBreed = registerViewModel.fetchDogBreed().first(),
                        dogSex = registerViewModel.fetchDogSex().first(),
                        isNeuter = registerViewModel.fetchDogNeuter().first(),
                        dogLikeTag = registerViewModel.fetchDogLikeTag().first(),
                        dogDislikeTag = registerViewModel.fetchDogDislikeTag().first(),
                        latitude = registerViewModel.fetchUserLatitude().first(),
                        longitude = registerViewModel.fetchUserLongitude().first(),
                        town = registerViewModel.fetchUserLocation().first()
                    )

                    registerViewModel.postRegisterInfo(imgFile, registerInfo)
                    Log.d("회원가입 때 작성한 내용", "${registerInfo}")

                    registerViewModel.deleteAllInfo()
                    prefs.edit().remove("resId").apply()
                }

                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)

                activity?.finish() //RegisterActivity 종료
            }

        }
    }

    // GPS on/off 확인
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
    private fun checkPermissionForLocation() {
        val isFirstCheck = prefs.getBoolean("isFistLocationPermissionCheck", true)
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates()
                showLoadingDialog(requireContext())
            }

            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) -> {
                showFirstPermissionDialog()
            }

            else -> {
                if (isFirstCheck) {
                    prefs.edit().putBoolean("isFistLocationPermissionCheck", false).apply()
                    locationPermissionRequest.launch(
                        arrayOf(
                            ACCESS_COARSE_LOCATION,
                            ACCESS_FINE_LOCATION
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
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION
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

    // 사용자 위도, 경도 받아오기 -> LocationManager 정확도 이슈
    private fun startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        fusedLocationClient!!.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT)
                        .show()
                else {
                    lifecycleScope.launch {
                        registerViewModel.saveUserLatitude(location.latitude)
                        registerViewModel.saveUserLongitude(location.longitude)
                    }
                    val geocoder = Geocoder(requireContext())
                    convertToAddress(geocoder, location)
                }
            }
    }

    // 위도, 경도 -> geocoder
    private fun convertToAddress(geocoder: Geocoder, it: Location) {
        val address = geocoder.getFromLocation(it.latitude, it.longitude, 1)

        val addressLine = address?.get(0)?.getAddressLine(0)
        val addressList = addressLine?.split(" ") as ArrayList<String>
        addressList.removeAt(0)
        addressList.removeAt(addressList.size - 1)

        // tvMyLocation에 넣을 주소 - '서울특별시 중구 다동'
        val locationAddress = StringBuilder()
        for (i in addressList) {
            locationAddress.append(i)
            locationAddress.append(" ")
        }
        binding.tvMyLocation.text = locationAddress

        lifecycleScope.launch {
            registerViewModel.saveUserLocation(locationAddress.toString()).apply { }
        }


        // 마이페이지에서 변환용 '중구 다동'만 저장
//        addressList.removeAt(0)
//        val addressForMyPage = StringBuilder()
//        for (i in addressList) {
//            addressForMyPage.append(i)
//            addressForMyPage.append(" ")
//        }

        dismissLoadingDialog()
        changeDoneButton()
    }


    // retrofit 통신
    private fun putLocationRetrofitWork(lat: Double, lng: Double, town: String) {
        val locationRetrofit = RetrofitApi.retrofit.create(LocationService::class.java)

        locationRetrofit.putLocationData(request = UserLocationRequest(lat, lng, town))
            .enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("set_location_again", response.toString())
                        Log.d("set_location_again", "성공")
                    } else {
                        Log.d("set_location_again", "실패")
                        Log.d("set_location_again", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("set_location_again", "실패")
                    Log.d("set_location_again", t.toString())
                }
            })
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}