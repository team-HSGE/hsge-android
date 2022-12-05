package com.starters.hsge.presentation.register.fragment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogPhotoBinding
import com.starters.hsge.domain.UriUtil
import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DogPhotoFragment : BaseFragment<FragmentDogPhotoBinding>(R.layout.fragment_dog_photo) {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val OPEN_GALLERY = 1
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //https://stackoverflow.com/questions/41928803/how-to-parse-json-in-kotlin
        val imageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    //이미지 받으면 ImageView에 적용
                    val imageUri = result.data?.data
                    registerViewModel.dogPhoto = imageUri.toString()
                    context?.let {
                        if (imageUri != null) {

                            val registerInfo = RegisterInfo(
                                email = "slee513@naver.com",
                                userNickName = "서유니",
                                userIcon = 221029,
                                dogAge = "ONE_MONTH",
                                dogName = "야미",
                                dogBreed = "SAMOYED",
                                dogSex = "여자",
                                dogNeuter = true,
                                dogLikeTag = "#강아지#고양이#사람",
                                dogDislikeTag = "#호랑이#오소리#너구리",
                                latitude = 321.3,
                                longitude = 222.1
                            )

                            val uriToFile = UriUtil.toFile(it, imageUri)

                            lifecycleScope.launch {
                                registerViewModel.postRegisterInfo(uriToFile, registerInfo)
                            }
                            setButtonEnable()
                        }
                    }
                    imageUri?.let {
                        //서버 업로드를 위해 파일 형태로 변환
                        //imageFile = File(getRealPathFromURI(it))

                        //이미지 불러오기
                        Glide.with(this)
                            .load(imageUri)
                            .fitCenter()
                            .circleCrop()
                            .into(binding.ivDogPhoto)
                    }
                }
            }

        // 권한 요청 -> launch로 권한 대화상자 열기
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted
                    // 갤러리 열기
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.setType("image/*")
                    startActivityForResult(intent, OPEN_GALLERY)

                    // 이미지 등록
                    imageResult.launch(intent)

                } else {
                    // Explain to the user
                }

            }

        initListener()
        setNavigation()

    }

    private fun initListener() {

        binding.ivDogPhoto.setOnClickListener {
            // 시스템 권한 대화상자 요청
            requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogPhotoFragment_to_dogNameFragment)
        }
    }

    private fun setButtonEnable() {
        binding.btnNext.isEnabled = !registerViewModel.dogPhoto.isNullOrEmpty()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}