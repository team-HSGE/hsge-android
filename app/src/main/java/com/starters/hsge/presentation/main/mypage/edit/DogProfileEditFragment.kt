package com.starters.hsge.presentation.main.mypage.edit

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
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.starters.hsge.R
import com.starters.hsge.common.constants.TagViewType
import com.starters.hsge.data.model.remote.request.EditDogRequest
import com.starters.hsge.databinding.FragmentDogProfileEditBinding
import com.starters.hsge.domain.usecase.GetDislikeTagsUseCase
import com.starters.hsge.domain.usecase.GetLikeTagsUseCase
import com.starters.hsge.domain.util.UriUtil
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.HideKeyboard.hideKeyboard
import com.starters.hsge.presentation.common.util.LoadingDialog
import com.starters.hsge.presentation.dialog.*
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DogProfileEditFragment :
    BaseFragment<FragmentDogProfileEditBinding>(R.layout.fragment_dog_profile_edit) {

    @Inject
    lateinit var getLikeTagsUseCase: GetLikeTagsUseCase
    @Inject
    lateinit var getDislikeTagsUseCase: GetDislikeTagsUseCase

    private val args: DogProfileEditFragmentArgs by navArgs()
    private lateinit var callback: OnBackPressedCallback

    private val registerViewModel: RegisterViewModel by viewModels()
    private val dogProfileEditViewModel: DogProfileEditViewModel by viewModels()

    private lateinit var ageBottomSheet: BottomSheetDialog
    private lateinit var breedBottomSheet: BottomSheetDialog

    private lateinit var storagePermissionRequest: ActivityResultLauncher<String>
    private lateinit var imageResult: ActivityResultLauncher<Intent>

    private lateinit var tagBottomSheetDialog: TagBottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPermissionLauncher()
        initImageLauncher()
        initValue()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dogDetail = args.dogDetailInfo
        binding.dogDetailInfo = dogDetail

        if (args.dogDetailInfo.gender == "남") {
            binding.rbtnMale.isChecked = true
        } else {
            binding.rbtnFemale.isChecked = true
        }

        initListener()
        createTagTextView(binding.likeChipsContainer, args.dogDetailInfo.tag.tagLike)
        createTagTextView(binding.dislikeChipsContainer, args.dogDetailInfo.tag.tagDisLike)
        setNavigation()
        Timber.d("리스트 ${getDislikeTagsUseCase.invoke()}")
    }

    private fun initValue() {
        with(dogProfileEditViewModel) {
            dogName = args.dogDetailInfo.petName
            dogSex = args.dogDetailInfo.gender
            dogNeuter = args.dogDetailInfo.neutralization
            dogAge = args.dogDetailInfo.ageDto.key
            dogBreed = args.dogDetailInfo.breedDto.key
            dogLikeTagStr = changeListToString(args.dogDetailInfo.tag.tagLike)
            dogDislikeTagStr = changeListToString(args.dogDetailInfo.tag.tagDisLike)
            dogLikeTag = args.dogDetailInfo.tag.tagLike
            dogDislikeTag = args.dogDetailInfo.tag.tagDisLike
            description = args.dogDetailInfo.description.toString()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showCancelDialog()
                goneBtmNav()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
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
                            dogProfileEditViewModel.dogPhoto = imgUriToStr
                            Glide.with(this)
                                .load(imageUri)
                                .fitCenter()
                                .circleCrop()
                                .into(binding.ivDogPhoto)
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
        // 반려견 프로필 사진
        binding.ivDogPhoto.setOnClickListener {
            checkPermission()
        }

        // 반려견 이름
        binding.dogNameEditSection.setOnClickListener {
            val dialog = EditNameDialogFragment(okBtnClickListener = {
                if (it.isNotEmpty() && it.isNotBlank()) {
                    binding.tvDogNameEdit.text = it
                    dogProfileEditViewModel.dogName = it
                }
            })
            dialog.show(childFragmentManager, EditNameDialogFragment.TAG)
        }

        //반려견 성별
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbtn_male -> {
                    dogProfileEditViewModel.dogSex = "남"
                }
                R.id.rbtn_female -> {
                    dogProfileEditViewModel.dogSex = "여"
                }
            }
        }

        // 반려견 나이 Dialog
        binding.tvDogAgeEdit.setOnClickListener {
            registerViewModel.ageMap.observe(viewLifecycleOwner) { age ->
                if (age != null) {
                    ageBottomSheet = BottomSheetDialog(age.keys.toList())
                    ageBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
                    ageBottomSheet.setBottomSheetClickListener(object :
                        BottomSheetDialog.BottomSheetClickListener {
                        override fun onContentClick(content: String) {
                            binding.tvDogAgeEdit.text = content
                            dogProfileEditViewModel.dogAge = age[content].toString()
                        }
                    })
                }
            }
        }

        // 반려견 품종 Dialog
        binding.tvDogBreedEdit.setOnClickListener {
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
                            binding.tvDogBreedEdit.text = newFormatBreed
                            dogProfileEditViewModel.dogBreed = breed[content].toString()
                        }
                    })
                }
            }
        }

        // like tag
        binding.dogLikeTagEditSection.setOnClickListener {
            tagBottomSheetDialog = TagBottomSheetDialog(
                getLikeTagsUseCase.invoke(),
                TagViewType.LIKE,
                dogProfileEditViewModel.dogLikeTag,
                okBtnClickListener = { tagList ->
                    // 기존 태그 view에서 삭제
                    binding.likeChipsContainer.apply {
                        removeAllViewsInLayout()
                    }
                    createTagTextView(binding.likeChipsContainer, tagList)
                    dogProfileEditViewModel.dogLikeTagStr = changeListToString(tagList)
                    dogProfileEditViewModel.dogLikeTag = tagList
                })
            tagBottomSheetDialog.show(childFragmentManager, TagBottomSheetDialog.TAG)
        }

        // dislike tag
        binding.dogDislikeTagEditSection.setOnClickListener {
            tagBottomSheetDialog = TagBottomSheetDialog(
                getDislikeTagsUseCase.invoke(),
                TagViewType.DISLIKE,
                dogProfileEditViewModel.dogDislikeTag,
                okBtnClickListener = { tagList ->
                    // 기존 태그 view에서 삭제
                    binding.dislikeChipsContainer.apply {
                        removeAllViewsInLayout()
                    }
                    createTagTextView(binding.dislikeChipsContainer, tagList)
                    dogProfileEditViewModel.dogDislikeTagStr = changeListToString(tagList)
                    dogProfileEditViewModel.dogDislikeTag = tagList
                })
            tagBottomSheetDialog.show(childFragmentManager, TagBottomSheetDialog.TAG)
        }

        // 수정하기
        binding.btnEdit.setOnClickListener {
            // loading progress
            LoadingDialog.showDogLoadingDialog(requireContext())

            val imgFile = dogProfileEditViewModel.dogPhoto?.toUri()
                ?.let { uri -> UriUtil.toFile(requireContext(), uri) }

            val dogProfileInfo = EditDogRequest(
                petName = dogProfileEditViewModel.dogName,
                gender = dogProfileEditViewModel.dogSex,
                age = dogProfileEditViewModel.dogAge,
                breed = dogProfileEditViewModel.dogBreed,
                neutralization = getDogNeuter(),
                description = getDescription(),
                likeTag = dogProfileEditViewModel.dogLikeTagStr,
                dislikeTag = dogProfileEditViewModel.dogDislikeTagStr
            )

            dogProfileEditViewModel.putEditDogProfile(
                args.dogDetailInfo.id,
                imgFile,
                dogProfileInfo
            )

            dogProfileEditViewModel.editResponse.observe(viewLifecycleOwner) {
                if (it.isSuccessful) {
                    LoadingDialog.dismissDogLoadingDialog()
                    showToast("수정이 완료되었습니다.")
                    findNavController().navigateUp()
                } else {
                    LoadingDialog.dismissDogLoadingDialog()
                }
            }
        }

        // 삭제하기
        binding.tvDeleteBtn.setOnClickListener {
            val dialog = ConfirmDialogFragment("프로필을 삭제하시겠습니까?")
            dialog.setButtonClickListener(object : ConfirmDialogFragment.OnButtonClickListener {
                override fun onCancelBtnClicked() {
                    // 취소 버튼 클릭했을 때 처리
                }
                override fun onOkBtnClicked() {
                    // loading progress
                    LoadingDialog.showDogLoadingDialog(requireContext())

                    dogProfileEditViewModel.deleteDog(args.dogDetailInfo.id)
                    dogProfileEditViewModel.deleteResponse.observe(viewLifecycleOwner) {
                        if (it.isSuccessful) {
                            LoadingDialog.dismissDogLoadingDialog()
                            showToast("삭제가 완료되었습니다.")
                            findNavController().navigateUp()
                        } else {
                            LoadingDialog.dismissDogLoadingDialog()
                        }
                    }
                }
            })
            dialog.show(childFragmentManager, "CustomDialog")
        }

        // 레이아웃 영역 터치
        binding.layoutContainer.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun getDescription(): String {
        dogProfileEditViewModel.description = binding.edtComment.text.toString()
        return dogProfileEditViewModel.description
    }

    private fun getDogNeuter(): Boolean {
        dogProfileEditViewModel.dogNeuter = binding.chipNeuter.isChecked
        return dogProfileEditViewModel.dogNeuter
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createTagTextView(container: LinearLayout, tagList: List<String>) {
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
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

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            showCancelDialog()
        }
    }

    private fun goneBtmNav() {
        (activity as MainActivity).binding.navigationMain.visibility = View.GONE
    }

    private fun showCancelDialog() {
        val dialog = ConfirmDialogFragment("수정을 취소하시겠습니까?")
        dialog.setButtonClickListener(object : ConfirmDialogFragment.OnButtonClickListener {
            override fun onCancelBtnClicked() {
            }

            override fun onOkBtnClicked() {
                findNavController().navigateUp()
            }
        })
        dialog.show(childFragmentManager, "CustomDialog")
    }
}