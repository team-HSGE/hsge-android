package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogDislikeTagBinding
import com.starters.hsge.domain.usecase.GetDislikeTagsUseCase
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DogDislikeTagFragment :
    BaseFragment<FragmentDogDislikeTagBinding>(R.layout.fragment_dog_dislike_tag) {

    @Inject
    lateinit var getDislikeTagsUseCase: GetDislikeTagsUseCase

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpChipGroupDynamically(getDislikeTagsUseCase.invoke())
        updateCheckedChip()
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
            binding.btnNext.isEnabled = ids.isNotEmpty()

            if (ids.size > 3) {
                chip.isChecked = false
            }
        }
        return chip
    }

    private fun getChipsText(): String {
        var dislikeTags = ""
        for (index in 0 until binding.chipGroupDislike.childCount) {
            val chip = binding.chipGroupDislike.getChildAt(index) as Chip
            if (binding.chipGroupDislike.checkedChipIds.contains(chip.id)) {
                dislikeTags += chip.text.toString() + ","
            }
        }
        return dislikeTags
    }

    private fun updateCheckedChip() {
        lifecycleScope.launch {
            if (registerViewModel.fetchDogDislikeTag().first().isNotEmpty()) {
                val tagList: List<String> =
                    registerViewModel.fetchDogDislikeTag().first().split(",")

                for (index in 0 until binding.chipGroupDislike.childCount) {
                    val chip = binding.chipGroupDislike.getChildAt(index) as Chip
                    if (tagList.contains(chip.text)) {
                        chip.isChecked = true
                    }
                }
                binding.btnNext.isEnabled = true
            }
        }
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogDislikeTagFragment_to_userLocationFragment)

            lifecycleScope.launch {
                registerViewModel.saveDogDislikeTag(getChipsText())
            }
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}