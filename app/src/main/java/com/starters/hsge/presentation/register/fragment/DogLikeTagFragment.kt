package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogLikeTagBinding
import com.starters.hsge.domain.usecase.GetLikeTagsUseCase
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DogLikeTagFragment : BaseFragment<FragmentDogLikeTagBinding>(R.layout.fragment_dog_like_tag) {

    @Inject
    lateinit var getLikeTagsUseCase: GetLikeTagsUseCase

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpChipGroupDynamically(getLikeTagsUseCase.invoke())
        updateCheckedChip()
        initListener()
        setNavigation()
    }

    private fun setUpChipGroupDynamically(chipList: List<String>) {
        for (i in chipList) {
            binding.chipGroup.addView(createChip(i))
        }
    }

    private fun createChip(label: String): Chip {
        val chip = Chip(context, null, R.attr.CustomLikeChipChoiceStyle)
        chip.text = label

        chip.setOnClickListener {
            val ids: List<Int> = binding.chipGroup.checkedChipIds
            binding.btnNext.isEnabled = ids.isNotEmpty()

            if (ids.size > 3) {
                chip.isChecked = false
            }
        }
        return chip
    }

    private fun getChipsText(): String {
        var likeTas = ""
        for (index in 0 until binding.chipGroup.childCount) {
            val chip = binding.chipGroup.getChildAt(index) as Chip
            if (binding.chipGroup.checkedChipIds.contains(chip.id)) {
                likeTas += chip.text.toString() + ","
            }
        }
        return likeTas
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogLikeTagFragment_to_dogDislikeTagFragment)

            lifecycleScope.launch {
                registerViewModel.saveDogLikeTag(getChipsText())
            }
        }
    }

    private fun updateCheckedChip() {
        lifecycleScope.launch {
            if (registerViewModel.fetchDogLikeTag().first().isNotEmpty()) {
                val tagList: List<String> = registerViewModel.fetchDogLikeTag().first().split(",")
                for (index in 0 until binding.chipGroup.childCount) {
                    val chip = binding.chipGroup.getChildAt(index) as Chip
                    if (tagList.contains(chip.text)) {
                        chip.isChecked = true
                    }
                }
                binding.btnNext.isEnabled = true
            }
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
