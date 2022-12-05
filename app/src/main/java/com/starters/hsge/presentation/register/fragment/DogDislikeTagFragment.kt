package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogDislikeTagBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogDislikeTagFragment : BaseFragment<FragmentDogDislikeTagBinding>(R.layout.fragment_dog_dislike_tag) {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf(
            "#남자사람", "#여자사람", "#아이", "#사람", "#암컷", "#대형견", "#중형견",
            "#소형견", "#옷입기", "#사진찍기", "#수영", "#뽀뽀", "#발만지기", "#꼬리만지기",
            "#스킨십", "#큰소리", "#향수")

        setUpChipGroupDynamically(list)
        initListener()
        setNavigation()

    }

    private fun setUpChipGroupDynamically(chipList: List<String>) {
        for (i in chipList) {
            binding.chipGroupDislike.addView(createChip(i))
        }
    }

    private fun createChip(label: String): Chip {
        val chip = Chip(context, null, R.attr.CustomDislikeChipChoiceStyle)
        chip.text = label
        chip.setOnClickListener {
            val ids: List<Int> = binding.chipGroupDislike.checkedChipIds
            if (ids.size == 3) {
                for (index in 0 until binding.chipGroupDislike.childCount) {
                    val chip = binding.chipGroupDislike.getChildAt(index) as Chip
                    chip.isCheckable = chip.isChecked
                }
            } else if (ids.size < 3) {
                for (index in 0 until binding.chipGroupDislike.childCount) {
                    val chip = binding.chipGroupDislike.getChildAt(index) as Chip
                    chip.isCheckable = true
                }
            }
        }
        return chip
    }

    private fun getChipsText(): String {
        var dislikeTags = ""
        for (index in 0 until binding.chipGroupDislike.childCount) {
            val chip = binding.chipGroupDislike.getChildAt(index) as Chip
            if (binding.chipGroupDislike.checkedChipIds.contains(chip.id)) {
                dislikeTags += chip.text
            }
        }
        return dislikeTags
    }


    private fun initListener() {
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogDislikeTagFragment_to_userLocationFragment)

            registerViewModel.dogDisLikeTag = getChipsText()
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}