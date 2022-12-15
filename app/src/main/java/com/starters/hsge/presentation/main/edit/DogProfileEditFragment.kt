package com.starters.hsge.presentation.main.edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogProfileEditBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.dialog.EditNameDialogFragment
import com.starters.hsge.presentation.dialog.TagBottomSheetDialog
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DogProfileEditFragment : BaseFragment<FragmentDogProfileEditBinding>(R.layout.fragment_dog_profile_edit) {

    private val args: DogProfileEditFragmentArgs by navArgs()

    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var ageBottomSheet: BottomSheetDialog
    private lateinit var breedBottomSheet: BottomSheetDialog

    private lateinit var storagePermissionRequest: ActivityResultLauncher<String>
    private lateinit var imageResult: ActivityResultLauncher<Intent>

    private lateinit var tagBottomSheetDialog: TagBottomSheetDialog

    private val likeTagList = listOf(
        "지방,", "여자사람", "아이", "사람", "암컷", "수컷", "공놀이", "터그놀이",
        "산책", "수영", "대형견", "중형견", "소형견", "옷입기", "사진찍기", "잠자기",
        "간식", "고구마", "닭가슴살", "야채", "과일", "단호박", "개껌", "인형"
    )

    private val dislikeTagList = listOf(
        "", "단거,", "지방,", "사람", "암컷", "대형견", "중형견",
        "소형견", "옷입기", "사진찍기", "수영", "뽀뽀", "발만지기", "꼬리만지기",
        "스킨십", "큰소리", "향수"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPermissionLauncher()
        initImageLauncher()

        val dogDetail = args.dogDetailInfo
        binding.dogDetailInfo = dogDetail

        if (args.dogDetailInfo.gender == "남") {
            binding.rbtnMale.isChecked = true
        } else {
            binding.rbtnFemale.isChecked = true
        }

        initListener()
        createLikeTagTextView()
        createDislikeTagTextView()
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
                            registerViewModel.img = imgUriToStr
                            lifecycleScope.launch {
                                registerViewModel.saveDogPhoto(imgUriToStr)
                            }

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
            //Custom EditText Dialog
            val dialog = EditNameDialogFragment()
            dialog.show(childFragmentManager, EditNameDialogFragment.TAG)

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
                        }
                    })
                }
            }
        }

        // like tag
        binding.dogLikeTagEditSection.setOnClickListener {
            tagBottomSheetDialog = TagBottomSheetDialog(likeTagList, ViewType.LIKE, args.dogDetailInfo.tag.tagLike)
            tagBottomSheetDialog.show(childFragmentManager, TagBottomSheetDialog.TAG)
        }

        // dislike tag
        binding.dogDislikeTagEditSection.setOnClickListener {
            tagBottomSheetDialog = TagBottomSheetDialog(dislikeTagList, ViewType.DISLIKE, args.dogDetailInfo.tag.tagDisLike)
            tagBottomSheetDialog.show(childFragmentManager, TagBottomSheetDialog.TAG)
        }

        // 수정하기
        binding.btnEdit.setOnClickListener {

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createLikeTagTextView() {
        for (i in args.dogDetailInfo.tag.tagLike) {
            val textView = TextView(requireContext())
            textView.text = i
            textView.background = resources.getDrawable(R.drawable.bg_g100_r14)
            textView.setPadding(30, 24, 30, 24)
            binding.likeChipsContainer.addView(textView)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createDislikeTagTextView() {
        for (i in args.dogDetailInfo.tag.tagDisLike) {
            val textView = TextView(requireContext())
            textView.text = i
            textView.background = resources.getDrawable(R.drawable.bg_g100_r14)
            textView.setPadding(30, 24, 30, 24)
            binding.dislikeChipsContainer.addView(textView)
        }
    }
}