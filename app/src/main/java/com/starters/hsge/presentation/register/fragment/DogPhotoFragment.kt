package com.starters.hsge.presentation.register.fragment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogPhotoBinding
import com.starters.hsge.domain.UriUtil
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogPhotoFragment : BaseFragment<FragmentDogPhotoBinding>(R.layout.fragment_dog_photo) {

    private lateinit var storagePermissionRequest: ActivityResultLauncher<String>
    private lateinit var imageResult: ActivityResultLauncher<Intent>
    private val registerViewModel: RegisterViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.isEnabled = true

        initPermissionLauncher()
        initImageLauncher()
        initListener()
        setNavigation()

    }

    private fun initPermissionLauncher() {
        storagePermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    navigateGallery()
                } else {
                    checkPermission()
                }
            }
    }

    private fun initImageLauncher() {
        imageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val imageUri = result.data?.data

                    context?.let {
                        if (imageUri != null) {
                            val imgFile = UriUtil.toFile(it, imageUri)
                            //registerViewModel.dogPhoto = imgFile
                            Glide.with(this)
                                .load(imageUri)
                                .fitCenter()
                                .circleCrop()
                                .into(binding.ivDogPhoto)

                            //setButtonEnable()
                        }
                    }
                }
            }
    }

    private fun checkPermission() {
        val isFirstCheck = prefs.getBoolean("isFistImgPermissionCheck", true)
        when {

            ContextCompat.checkSelfPermission(
                requireContext(),
                READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                navigateGallery()
            }

            shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) -> {
                showFirstPermissionDialog()
            }

            else -> {
                if (isFirstCheck) {
                    prefs.edit().putBoolean("isFistImgPermissionCheck", false).apply()
                    storagePermissionRequest.launch(READ_EXTERNAL_STORAGE)
                } else {
                    showSecondPermissionDialog()
                }
            }
        }
    }

    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imageResult.launch(intent)
    }

    private fun showFirstPermissionDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("앱 실행을 위해서는 권한을 설정해야 합니다")
            .setPositiveButton("확인") { _, _ ->
                storagePermissionRequest.launch(READ_EXTERNAL_STORAGE)
            }
            .show()
    }

    private fun showSecondPermissionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("설정에서 권한을 허용해주세요")
        builder.setPositiveButton("설정으로 이동하기") { _, _ ->
            val intent = Intent(
                ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                    "package", requireContext().packageName, null
                )
            )
            startActivity(intent)
        }
        builder.show()
    }


    private fun initListener() {

        binding.ivDogPhoto.setOnClickListener {
            checkPermission()
        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogPhotoFragment_to_dogNameFragment)
        }
    }

    private fun setButtonEnable() {
        binding.btnNext.isEnabled = !registerViewModel.dogPhoto.exists()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}