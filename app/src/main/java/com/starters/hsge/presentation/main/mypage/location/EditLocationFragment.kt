package com.starters.hsge.presentation.main.mypage.location

import android.Manifest
import android.content.Context
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
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import com.starters.hsge.R
import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.databinding.FragmentEditLocationBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.LoadingDialog
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.location.EditLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EditLocationFragment :
    BaseFragment<FragmentEditLocationBinding>(R.layout.fragment_edit_location) {

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val editLocationViewModel: EditLocationViewModel by viewModels()
    private val args: EditLocationFragmentArgs by navArgs()
    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPermissionLauncher()
        initValue()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userLocation = args.locationInfo
        initListener()
        setNavigation()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
                visibleBtmNav()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
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


    private fun initValue() {
        args.locationInfo.let {
            editLocationViewModel.latitude = args.locationInfo.latitude
            editLocationViewModel.longitude = args.locationInfo.longitude
            editLocationViewModel.town = args.locationInfo.town
        }
    }

    private fun initListener() {
        binding.btnSearch.setOnClickListener {
            // 사용자 위치 받아오기
            setAutoLocation()
        }

        binding.btnFinish.setOnClickListener {
            // loading progress
            LoadingDialog.showDogLoadingDialog(requireContext())

            //위치 전송
            editLocationViewModel.putLocation(
                UserLocationRequest(
                    latitude = editLocationViewModel.latitude,
                    longtitude = editLocationViewModel.longitude,
                    town = editLocationViewModel.town
                )
            )

            editLocationViewModel.mResponse.observe(viewLifecycleOwner) {
                if (it.isSuccessful) {
                    LoadingDialog.dismissDogLoadingDialog()
                    findNavController().navigateUp()
                    visibleBtmNav()
                } else {
                    LoadingDialog.dismissDogLoadingDialog()
                }
            }
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            visibleBtmNav()
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
            ContextCompat.checkSelfPermission( // 허용
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> { // 거부
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
                } else { // 다시 묻지 않음 (두 번 거부)
                    Timber.d("여기 뭐냐 $isFirstCheck")
                    Timber.d("다시 안 물어가 여기고")
                    showSecondPermissionDialog()
                }
            }
        }
    }

    private fun showNeedGPSDialog(){
        AlertDialog.Builder(requireContext())
            .setMessage("위치 사용이 필요합니다")
            .setPositiveButton("확인") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
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

    // 사용자 위도, 경도 받아오기 -> LocationManager 정확도 이슈
    private fun startLocationUpdates() {
        LoadingDialog.showLocationLoadingDialog(requireContext())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
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
                    editLocationViewModel.latitude = location.latitude
                    editLocationViewModel.longitude = location.longitude
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
        editLocationViewModel.town = locationAddress.toString()
        LoadingDialog.dismissLocationLoadingDialog()
    }

    private fun visibleBtmNav() {
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }
}
