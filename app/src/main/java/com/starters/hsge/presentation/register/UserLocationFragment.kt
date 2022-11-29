package com.starters.hsge.presentation.register

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserLocationBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class UserLocationFragment :
    BaseFragment<FragmentUserLocationBinding>(R.layout.fragment_user_location) {

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                }
                else -> {
                    // No location access granted.
                }
            }
        }

        initListener()

    }

    private fun initListener() {
        binding.btnSearch.setOnClickListener {
            checkLocationPermission()
        }
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userLocationFragment_to_radiusFragment)
        }
    }

    private fun checkLocationPermission() {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationGranted && !coarseLocationGranted) {
            // 권한이 없다면 시스템 권한 대화상자 요청
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            // 권한이 있으면 현재 위치 도로명 주소로 변환
            val geocoder = Geocoder(requireContext())
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

            // LocationRequest 생성
            val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
                interval = 1000
                priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val client = LocationServices.getSettingsClient(requireContext())
            val task = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                // location client setting success
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        val address = geocoder.getFromLocation(it.latitude, it.longitude, 1)

                        val latitude = it.latitude
                        val longitude = it.longitude

                        val addressLine = address?.get(0)?.getAddressLine(0)
                        val addressList = addressLine?.split(" ") as ArrayList<String>
                        addressList.removeAt(0)
                        addressList.removeAt(addressList.size - 1)

                        val locationAddress = StringBuilder()
                        for (i in addressList) {
                            locationAddress.append(i)
                            locationAddress.append(" ")
                        }

                        Toast.makeText(requireContext(), "$locationAddress", Toast.LENGTH_SHORT)
                            .show()
                        binding.tvMyLocation.text = locationAddress

                    } else {
                        Toast.makeText(requireContext(), "Location이 null임", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            task.addOnFailureListener {
                // location client setting failure
                Toast.makeText(requireContext(), "location client setting failure", Toast.LENGTH_SHORT).show()
            }
        }
    }
}