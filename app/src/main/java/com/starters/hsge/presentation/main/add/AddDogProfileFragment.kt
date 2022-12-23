package com.starters.hsge.presentation.main.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.starters.hsge.R
import com.starters.hsge.data.model.remote.request.AddDogRequest
import com.starters.hsge.databinding.FragmentAddDogProfileBinding
import com.starters.hsge.domain.UriUtil
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.dialog.TagBottomSheetDialog
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.edit.ViewType
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDogProfileFragment :
    BaseFragment<FragmentAddDogProfileBinding>(R.layout.fragment_add_dog_profile) {

    private lateinit var ageBottomSheet: BottomSheetDialog
    private lateinit var breedBottomSheet: BottomSheetDialog

    private lateinit var storagePermissionRequest: ActivityResultLauncher<String>
    private lateinit var imageResult: ActivityResultLauncher<Intent>

    private val registerViewModel: RegisterViewModel by viewModels()
    private val addDogProfileViewModel: AddDogProfileViewModel by viewModels()

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
                likeTagList,
                ViewType.LIKE,
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
                dislikeTagList,
                ViewType.DISLIKE,
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
                Toast.makeText(context, "사진을 등록해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.edtDogNameInput.text.isNullOrEmpty()) {
                Toast.makeText(context, "이름을 적어주세요", Toast.LENGTH_SHORT).show()
            } else if (addDogProfileViewModel.dogSex.isEmpty()) {
                Toast.makeText(context, "성별을 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (addDogProfileViewModel.dogAge.isEmpty()) {
                Toast.makeText(context, "나이를 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (addDogProfileViewModel.dogBreed.isEmpty()) {
                Toast.makeText(context, "품종을 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (addDogProfileViewModel.dogLikeTag.isEmpty()) {
                Toast.makeText(context, "like 태그를 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (addDogProfileViewModel.dogDislikeTag.isEmpty()) {
                Toast.makeText(context, "dislike 태그를 선택해주세요", Toast.LENGTH_SHORT).show()
            } else {
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

                visibleBtmNav()
                Toast.makeText(context, "반려견이 추가되었습니다", Toast.LENGTH_SHORT).show()

                // 마이페이지로 이동
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_addDogProfileFragment_to_myPageFragment)
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

    private fun visibleBtmNav() { (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE }
}