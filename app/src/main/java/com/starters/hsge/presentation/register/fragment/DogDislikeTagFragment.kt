package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogDislikeTagBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class DogDislikeTagFragment : BaseFragment<FragmentDogDislikeTagBinding>(R.layout.fragment_dog_dislike_tag) {

    lateinit var chip: Chip

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf(
            "#남자사람", "#여자사람", "#아이", "#사람", "#암컷", "#대형견", "#중형견",
            "#소형견", "#옷입기", "#사진찍기", "#수영", "#뽀뽀", "#발만지기", "#꼬리만지기",
            "#스킨십", "#큰소리", "#향수")

        initListener()
        initChipButton(list)
        setNavigation()
    }

    private fun initChipButton(chipList: List<String>) {
        for (i in chipList) {
            chip = Chip(context, null, R.attr.CustomDislikeChipChoiceStyle)
            chip.text = i
            binding.chipGroupDislike.addView(chip)
        }
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogDislikeTagFragment_to_userLocationFragment)
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}