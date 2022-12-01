package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogLikeTagBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogLikeTagFragment : BaseFragment<FragmentDogLikeTagBinding>(R.layout.fragment_dog_like_tag) {

    //칩버튼 동적추가 viewmodel
    // https://chachas.tistory.com/71
    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var chip: Chip
    private lateinit var Ids: List<Int>

    val list = listOf(
        "#남자사람", "#여자사람", "#아이", "#사람", "#암컷", "#수컷", "#공놀이", "#터그놀이",
        "#산책", "#수영", "#대형견", "#중형견", "#소형견", "#옷입기", "#사진찍기", "#잠자기",
        "#간식", "#고구마", "#닭가슴살", "#야채", "#과일", "#단호박", "#개껌", "#인형"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        initChipButton(list)
        //setChipCheckable()
        //setButtonEnable()

    }

    //https://choi3950.tistory.com/39
    private fun initChipButton(chipList: List<String>) {
        for (i in chipList) {
            chip = Chip(context, null, R.attr.CustomLikeChipChoiceStyle)
            chip.text = i
            binding.chipGroup.addView(chip)
            //setChipCheckable()
            chipChecked()
        }
    }

    private fun initListener() {

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogLikeTagFragment_to_dogDislikeTagFragment)
            for (i in 0..binding.chipGroup.checkedChipIds.size) {
                // chip id로 chip text 가져오기
                // chip string 붙이기
                // registerViewModel.dogLikTag += chip.text
                // 식별

            }
        }
    }

    private fun chipChecked() {
        binding.chipGroup.checkedChipIds.forEach {
            if (chip.id == it) {
                chip.setOnClickListener {
                    Ids = binding.chipGroup.checkedChipIds
                    if (Ids.size > 3) {
                        chip.isCheckable = false

                    }
                }
            }
        }

    }

    private fun setChipCheckable() {
        chip.setOnCheckedChangeListener { _, boolean ->
            if (boolean) {
                val ids: List<Int> = binding.chipGroup.checkedChipIds
                if (ids.size > 3) {
                    binding.chipGroup.checkedChipIds.forEach {
                        if (!ids.contains(it)) {
                            chip.isCheckable = false
                        }

                    }

                }
            }
        }
    }

    private fun setButtonEnable() {
        binding.btnNext.isEnabled = !registerViewModel.dogLikTag.isNullOrEmpty()
    }
}


//ChipGroup chipGroup = findViewById(R.id.category_chip_group);
//..
//chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//    @Override public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//        if (checked) {
//            //Get all checked chips in the group
//            List<Integer> ids = chipGroup.getCheckedChipIds();
//            if (ids.size() > 5) {
//                chip.setChecked(false);  //force to unchecked the chip
//            }
//        }
//    }
//});