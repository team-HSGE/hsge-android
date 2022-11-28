package com.starters.hsge.presentation.register.fragment

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
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserLocationBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class UserLocationFragment :
    BaseFragment<FragmentUserLocationBinding>(R.layout.fragment_user_location) {

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
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
        // 시스템 권한 대화상자 요청
        binding.btnSearch.setOnClickListener {
            checkLocationPermission()
//            locationPermissionRequest.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userLocationFragment_to_userDistanceFragment)
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

            Toast.makeText(context, "$fineLocationGranted + if", Toast.LENGTH_SHORT).show()

            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            val geocoder = Geocoder(requireContext())
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    val address = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    val korAddress = address?.get(0)
                    val location =
                        korAddress?.adminArea + " " + korAddress?.subLocality + " " + korAddress?.thoroughfare
                    binding.tvMyLocation.text = location
                } else {
                    Toast.makeText(requireContext(), "Location이 null임", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}