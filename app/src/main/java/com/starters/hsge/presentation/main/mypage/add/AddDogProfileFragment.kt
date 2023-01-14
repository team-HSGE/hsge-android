package com.starters.hsge.presentation.main.mypage.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.starters.hsge.R
import com.starters.hsge.common.constants.TagViewType
import com.starters.hsge.data.model.remote.request.AddDogRequest
import com.starters.hsge.databinding.FragmentAddDogProfileBinding
import com.starters.hsge.domain.usecase.GetDislikeTagsUseCase
import com.starters.hsge.domain.usecase.GetLikeTagsUseCase
import com.starters.hsge.domain.util.UriUtil
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.LoadingDialog
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.dialog.ConfirmDialogFragment
import com.starters.hsge.presentation.dialog.TagBottomSheetDialog
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddDogProfileFragment :
    BaseFragment<FragmentAddDogProfileBinding>(R.layout.fragment_add_dog_profile) {

    @Inject
    lateinit var getLikeTagsUseCase: GetLikeTagsUseCase
    @Inject
    lateinit var getDislikeTagsUseCase: GetDislikeTagsUseCase

    private lateinit var ageBottomSheet: BottomSheetDialog
    private lateinit var breedBottomSheet: BottomSheetDialog

    private lateinit var storagePermissionRequest: ActivityResultLauncher<String>
    private lateinit var imageResult: ActivityResultLauncher<Intent>

    private val registerViewModel: RegisterViewModel by viewModels()
    private val addDogProfileViewModel: AddDogProfileViewModel by viewModels()

    private lateinit var tagBottomSheetDialog: TagBottomSheetDialog

    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPermissionLauncher()
        initImageLauncher()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri = result.data?.data

                    context?.let {
                        if (imageUri != null) {
                            val imgUriToStr = imageUri.toString()
                            addDogProfileViewModel.dogPhoto = imgUriToStr
                            Glide.with(this)
                                .load(imageUri)
                                .fitCenter()
                                .circleCrop()
                                .into(binding.ivDogPhoto)

                            binding.ivDogPhotoEdit.visibility = View.VISIBLE
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
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                navigateGallery()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showFirstPermissionDialog()
            }
            else -> {
                if (isFirstCheck) {
                    prefs.edit().putBoolean("isFistImgPermissionCheck", false).apply()
                    storagePermissionRequest.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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
                storagePermissionRequest.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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

    private fun initListener() {
        // 강아지 사진
        binding.ivDogPhoto.setOnClickListener {
            checkPermission()
        }

        // 성별 선택
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbtn_male -> {
                    addDogProfileViewModel.dogSex = "남"
                }
                R.id.rbtn_female -> {
                    addDogProfileViewModel.dogSex = "여"
                }
            }
        }

        // 나이 선택
        binding.tvDogAgeInput.setOnClickListener {
            registerViewModel.ageMap.observe(viewLifecycleOwner) { age ->
                if (age != null) {
                    ageBottomSheet = BottomSheetDialog(age.keys.toList())
                    ageBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
                    ageBottomSheet.setBottomSheetClickListener(object :
                        BottomSheetDialog.BottomSheetClickListener {
                        override fun onContentClick(content: String) {
                            binding.tvDogAgeInput.text = content
                            addDogProfileViewModel.dogAge = age[content].toString()
                        }
                    })
                }
            }
        }

        // 품종 선택
        binding.tvDogBreedInput.setOnClickListener {
            registerViewModel.breedMap.observe(viewLifecycleOwner) { breed ->
                if (breed != null) {
                    breedBottomSheet = BottomSheetDialog(breed.keys.toList())
                    breedBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
                    breedBottomSheet.setBottomSheetClickListener(object :
                        BottomSheetDialog.BottomSheetClickListener {
                        override fun onContentClick(content: String) {
                            var newFormatBreed = content
                            if (content.length > 5) {
                                newFormatBreed = content.replace(" ", "\n")
                            }
                            binding.tvDogBreedInput.text = newFormatBreed
                            addDogProfileViewModel.dogBreed = breed[content].toString()
                        }
                    })
                }
            }
        }

        // like chip 선택
        binding.likeChipsContainer.setOnClickListener {
            tagBottomSheetDialog = TagBottomSheetDialog(
                getLikeTagsUseCase.invoke(),
                TagViewType.LIKE,
                addDogProfileViewModel.dogLikeTag,
                okBtnClickListener = { tagList ->
                    // 기존 태그 view에서 삭제
                    binding.likeChipsContainer.apply {
                        removeAllViewsInLayout()
                    }
                    createTagTextView(binding.likeChipsContainer, tagList)
                    addDogProfileViewModel.dogLikeTagStr = changeListToString(tagList)
                    addDogProfileViewModel.dogLikeTag = tagList
                })
            tagBottomSheetDialog.show(childFragmentManager, TagBottomSheetDialog.TAG)
        }

        // dislike chip 선택
        binding.dislikeChipsContainer.setOnClickListener {
            tagBottomSheetDialog = TagBottomSheetDialog(
                getDislikeTagsUseCase.invoke(),
                TagViewType.DISLIKE,
                addDogProfileViewModel.dogDislikeTag,
                okBtnClickListener = { tagList ->
                    // 기존 태그 view에서 삭제
                    binding.dislikeChipsContainer.apply {
                        removeAllViewsInLayout()
                    }
                    createTagTextView(binding.dislikeChipsContainer, tagList)
                    addDogProfileViewModel.dogDislikeTagStr = changeListToString(tagList)
                    addDogProfileViewModel.dogDislikeTag = tagList
                })
            tagBottomSheetDialog.show(childFragmentManager, TagBottomSheetDialog.TAG)
        }

        // 완료 버튼
        binding.btnFinish.setOnClickListener {
            if (addDogProfileViewModel.dogPhoto.isEmpty()) {
                showToast("사진을 등록해주세요")
            } else if (binding.edtDogNameInput.text.isNullOrEmpty()) {
                showToast("이름을 적어주세요")
            } else if (addDogProfileViewModel.dogSex.isEmpty()) {
                showToast("성별을 선택해주세요")
            } else if (addDogProfileViewModel.dogAge.isEmpty()) {
                showToast("나이를 선택해주세요")
            } else if (addDogProfileViewModel.dogBreed.isEmpty()) {
                showToast("품종을 선택해주세요")
            } else if (addDogProfileViewModel.dogLikeTag.isEmpty()) {
                showToast("like 태그를 선택해주세요")
            } else if (addDogProfileViewModel.dogDislikeTag.isEmpty()) {
                showToast("dislike 태그를 선택해주세요")
            } else {
                // loading progress
                LoadingDialog.showDogLoadingDialog(requireContext())

                val imgUri = addDogProfileViewModel.dogPhoto.toUri()
                val imgFile = UriUtil.toFile(requireContext(), imgUri)

                addDogProfileViewModel.postDogProfile(
                    imgFile,
                    AddDogRequest(
                        petName = binding.edtDogNameInput.text.toString(),
                        gender = addDogProfileViewModel.dogSex,
                        neutralization = binding.chipNeuter.isChecked,
                        age = addDogProfileViewModel.dogAge,
                        breed = addDogProfileViewModel.dogBreed,
                        likeTag = addDogProfileViewModel.dogLikeTagStr,
                        dislikeTag = addDogProfileViewModel.dogDislikeTagStr,
                        description = binding.edtComment.text.toString()
                    ))

                addDogProfileViewModel.mResponse.observe(viewLifecycleOwner) {
                    if (it.isSuccessful) {
                        LoadingDialog.dismissDogLoadingDialog()
                        showToast("반려견이 추가되었습니다")
                        findNavController().navigateUp()
                    } else {
                        LoadingDialog.dismissDogLoadingDialog()
                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createTagTextView(container: LinearLayout, tagList: List<String>) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, 30, 0)
        for (i in tagList) {
            val textView = TextView(requireContext())
            textView.text = i
            textView.background = resources.getDrawable(R.drawable.bg_g200_r16, null)
            textView.setPadding(34, 22, 34, 22)
            container.addView(textView)
            textView.layoutParams = layoutParams
        }
    }

    private fun changeListToString(list: List<String>): String {
        var tagText = ""
        for (i in list) {
            tagText += "$i,"
        }
        return tagText
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showCancelDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            showCancelDialog()
        }
    }

    private fun showCancelDialog() {
        val dialog = ConfirmDialogFragment("추가를 취소하시겠습니까?")
        dialog.setButtonClickListener(object : ConfirmDialogFragment.OnButtonClickListener {
            override fun onCancelBtnClicked() {
            }
            override fun onOkBtnClicked() {
                findNavController().navigateUp()
            }
        })
        dialog.show(childFragmentManager, "ConfirmDialog")
    }
}