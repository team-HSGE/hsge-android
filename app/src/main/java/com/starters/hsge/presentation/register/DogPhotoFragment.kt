package com.starters.hsge.presentation.register

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogPhotoBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class DogPhotoFragment : BaseFragment<FragmentDogPhotoBinding>(R.layout.fragment_dog_photo) {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val OPEN_GALLERY = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 권한 요청 -> launch로 권한 대화상자 열기
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted
                    // 갤러리 열기
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.setType("image/*")
                    startActivityForResult(intent, OPEN_GALLERY)

                } else {
                    // Explain to the user
                }

            }

        initListener()
    }

    private fun initListener() {

        binding.ivDogPhoto.setOnClickListener {
            // 시스템 권한 대화상자 요청
            requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
        }

        binding.tvNextButton.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogPhotoFragment_to_userLocationFragment)

        }
    }
}