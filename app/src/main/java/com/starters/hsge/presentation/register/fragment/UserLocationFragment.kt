package com.starters.hsge.presentation.register.fragment

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserLocationBinding
import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.domain.util.UriUtil
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.constants.*
import com.starters.hsge.presentation.common.util.LoadingDialog
import com.starters.hsge.presentation.onboarding.OnBoardingActivity
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

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
        lifecycleScope.launch {
            if (!registerViewModel.fetchUserLocation().first().isNullOrEmpty()) {
                binding.tvMyLocation.text = registerViewModel.fetchUserLocation().first()
                changeDoneButton()
            }
        }

        initPermissionLauncher()
        initListener()
        setNavigation()

    }

    private fun initPermissionLauncher() {
        locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                var valueList = arrayListOf<Boolean>()
                for (entry in it.entries) {
                    valueList.add( entry.value)
                }
                when {
                    !valueList.contains(false) -> { // 권한 허용 시
                        startLocationUpdates()
                    }
                    else -> { // 권한 거부
                        showToast("위치 권한 거부 시 위치 설정 기능을 이용할 수 없습니다.")
                    }
                }
            }
    }

    private fun changeDoneButton() {
        binding.btnNext.isEnabled = !binding.tvMyLocation.text.isNullOrEmpty()
    }

    private fun initListener() {
        // 사용자 위치 정보 받기
        binding.btnSearch.setOnClickListener {
            // GPS check & checkPermission
            setAutoLocation()
        }

        // 회원가입 완료 버튼
        binding.btnNext.setOnClickListener {
            // 로딩 다이얼로그
            LoadingDialog.showDogLoadingDialog(requireContext())

            lifecycleScope.launch {
                // 저장된 이미지 타입 변환: String -> Uri -> File
                val imgUri = registerViewModel.fetchDogPhoto().first().toUri()
                val imgFile = UriUtil.toFile(requireContext(), imgUri)

                val registerInfo = RegisterInfo(
                    email = prefs.getString("email", ""),
                    userNickName = registerViewModel.fetchUserNickname().first(),
                    userIcon = prefs.getInt(SAVE_RESID_ORDER, 0),
                    dogName = registerViewModel.fetchDogName().first(),
                    dogAge = registerViewModel.fetchDogAge().first(),
                    dogBreed = registerViewModel.fetchDogBreed().first(),
                    dogSex = registerViewModel.fetchDogSex().first(),
                    isNeuter = registerViewModel.fetchDogNeuter().first(),
                    dogLikeTag = registerViewModel.fetchDogLikeTag().first(),
                    dogDislikeTag = registerViewModel.fetchDogDislikeTag().first(),
                    latitude = registerViewModel.fetchUserLatitude().first(),
                    longitude = registerViewModel.fetchUserLongitude().first(),
                    town = registerViewModel.fetchUserLocation().first(),
                    fcmToken = prefs.getString("fcmToken", "")!!
                )

                registerViewModel.postRegisterInfo(imgFile, registerInfo)
                Timber.d("회원가입 때 작성한 내용 $registerInfo")

                registerViewModel.mResponse.observe(viewLifecycleOwner) {
                    if (it.isSuccessful) {
                        prefs.edit().putString(BEARER_ACCESS_TOKEN, "Bearer ${it.body()?.accessToken}").apply()
                        prefs.edit().putString(BEARER_REFRESH_TOKEN, "Bearer ${it.body()?.refreshToken}").apply()
                        prefs.edit().putString(NORMAL_ACCESS_TOKEN, it.body()?.accessToken).apply()
                        prefs.edit().putString(NORMAL_REFRESH_TOKEN, it.body()?.refreshToken).apply()

                        // 데이터 지우기
                        lifecycleScope.launch {
                            registerViewModel.deleteAllInfo()
                            prefs.edit().remove(SAVE_RESID_ORDER).apply()
                            prefs.edit().remove(SAVE_RESID_FOR_VIEW).apply()
                        }

                        // 로딩 다이얼로그 해제
                        LoadingDialog.dismissDogLoadingDialog()

                        // 액티비티 이동
                        val intent = Intent(context, OnBoardingActivity::class.java)
                        startActivity(intent)
                        activity?.finish()

                    } else {
                        // TODO: 유저 닉네임 설정 화면으로 이동
                        // 로딩 다이얼로그 해제
                        LoadingDialog.dismissDogLoadingDialog()
                    }
                }
            }
        }
    }

    // GPS 꺼져있을 경우
    private fun setAutoLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500).apply {
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
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

    // 권한 체크
    private fun checkPermissionForLocation() {
        val isFirstCheck = prefs.getBoolean("isFistLocationPermissionCheck", true)
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // TODO 색 바꾸기
                startLocationUpdates()
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
        LoadingDialog.showLocationLoadingDialog(requireContext())

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
                    showToast("Cannot get location.")
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

        // tvMyLocation에 넣을 주소 - '서울특별시 중구 다동 '
        val locationAddress = StringBuilder()
        for (i in addressList) {
            locationAddress.append(i)
            locationAddress.append(" ")
        }
        val town = locationAddress.dropLast(1).toString()
        binding.tvMyLocation.text = town

        lifecycleScope.launch {
            registerViewModel.saveUserLocation(town).apply { }
            registerViewModel.saveUserLocation(locationAddress.toString()).apply { }
        }

        LoadingDialog.dismissLocationLoadingDialog()
        changeDoneButton()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}