package com.starters.hsge.presentation.main.edit

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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.starters.hsge.R
import com.starters.hsge.data.model.remote.request.EditDogProfileRequest
import com.starters.hsge.databinding.FragmentDogProfileEditBinding
import com.starters.hsge.domain.UriUtil
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BaseDialogFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.dialog.EditNameDialogFragment
import com.starters.hsge.presentation.dialog.TagBottomSheetDialog
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogProfileEditFragment :
    BaseFragment<FragmentDogProfileEditBinding>(R.layout.fragment_dog_profile_edit) {

    private val args: DogProfileEditFragmentArgs by navArgs()
    private lateinit var callback: OnBackPressedCallback

    private val registerViewModel: RegisterViewModel by viewModels()
    private val dogProfileEditViewModel: DogProfileEditViewModel by viewModels()

    private lateinit var ageBottomSheet: BottomSheetDialog
    private lateinit var breedBottomSheet: BottomSheetDialog

    private lateinit var storagePermissionRequest: ActivityResultLauncher<String>
    private lateinit var imageResult: ActivityResultLauncher<Intent>

    private lateinit var tagBottomSheetDialog: TagBottomSheetDialog


    private val likeTagList = listOf(
        "남자사람", "여자사람", "아이", "사람", "암컷", "수컷", "공놀이", "터그놀이",
        "산책", "수영", "대형견", "중형견", "소형견", "옷입기", "사진찍기", "잠자기",
        "간식", "고구마", "닭가슴살", "야채", "과일", "단호박", "개껌", "인형"
    )

    private val dislikeTagList = listOf(
        "남자사람", "여자사람", "아이", "사람", "암컷", "수컷", "대형견", "중형견",
        "소형견", "옷입기", "사진찍기", "수영", "뽀뽀", "발만지기", "꼬리만지기",
        "스킨십", "큰소리", "향수"
    )

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
    }

    private fun initValue() {
        dogProfileEditViewModel.dogName = args.dogDetailInfo.petName
        dogProfileEditViewModel.dogSex = args.dogDetailInfo.gender
        dogProfileEditViewModel.dogNeuter = args.dogDetailInfo.neutralization
        dogProfileEditViewModel.dogAge = args.dogDetailInfo.ageDto.key
        dogProfileEditViewModel.dogBreed = args.dogDetailInfo.breedDto.key
        dogProfileEditViewModel.dogLikeTagStr = changeListToString(args.dogDetailInfo.tag.tagLike)
        dogProfileEditViewModel.dogDislikeTagStr =
            changeListToString(args.dogDetailInfo.tag.tagDisLike)
        dogProfileEditViewModel.dogLikeTag = args.dogDetailInfo.tag.tagLike
        dogProfileEditViewModel.dogDislikeTag = args.dogDetailInfo.tag.tagDisLike
        dogProfileEditViewModel.description = args.dogDetailInfo.description.toString()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
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
                            binding.tvDogBreedEdit.text = content
                            dogProfileEditViewModel.dogBreed = breed[content].toString()
                        }
                    })
                }
            }
        }

        // like tag
        binding.dogLikeTagEditSection.setOnClickListener {
            tagBottomSheetDialog = TagBottomSheetDialog(
                likeTagList,
                ViewType.LIKE,
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
                dislikeTagList,
                ViewType.DISLIKE,
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

            val imgFile = dogProfileEditViewModel.dogPhoto?.toUri()
                ?.let { uri -> UriUtil.toFile(requireContext(), uri) }

            val dogProfileInfo = EditDogProfileRequest(
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

            visibleBtmNav()
            Toast.makeText(context, "반려견 프로필이 수정되었습니다", Toast.LENGTH_SHORT).show()

            // 마이페이지로 이동
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogProfileEditFragment_to_myPageFragment)
        }

        binding.tvDeleteBtn.setOnClickListener {
            val dialog = BaseDialogFragment("프로필을 삭제하시겠습니까?")
            dialog.setButtonClickListener(object: BaseDialogFragment.OnButtonClickListener {
                override fun onCancelBtnClicked() {
                    // 취소 버튼 클릭했을 때 처리
                }

                override fun onOkBtnClicked() {
                    dogProfileEditViewModel.deleteDog(args.dogDetailInfo.id)
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_dogProfileEditFragment_to_myPageFragment)
                }
            })
            dialog.show(childFragmentManager, "CustomDialog")
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
            textView.background = resources.getDrawable(R.drawable.bg_g200_r14, null)
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
            findNavController().navigateUp()
        }
    }

    private fun goneBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.GONE
    }

    private fun visibleBtmNav() { (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE }
}