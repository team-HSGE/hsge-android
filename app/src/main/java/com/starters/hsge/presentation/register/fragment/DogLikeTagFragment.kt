package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogLikeTagBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DogLikeTagFragment : BaseFragment<FragmentDogLikeTagBinding>(R.layout.fragment_dog_like_tag) {

    private val registerViewModel: RegisterViewModel by viewModels()

    val list = listOf(
        "#남자사람", "#여자사람", "#아이", "#사람", "#암컷", "#수컷", "#공놀이", "#터그놀이",
        "#산책", "#수영", "#대형견", "#중형견", "#소형견", "#옷입기", "#사진찍기", "#잠자기",
        "#간식", "#고구마", "#닭가슴살", "#야채", "#과일", "#단호박", "#개껌", "#인형"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpChipGroupDynamically(list)
        //updateCheckedChip()
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

            lifecycleScope.launch {
                val ids: List<Int> = binding.chipGroup.checkedChipIds
                Log.d("선택된 칩을 보자", "${ids}")
                binding.btnNext.isEnabled = ids.isNotEmpty()
                var savedList: List<String> = registerViewModel.fetchDogLikeTag().first().split(",")
                if (ids.size == 3 || savedList.size == 3) {
                    for (index in 0 until binding.chipGroup.childCount) {
                        val chip = binding.chipGroup.getChildAt(index) as Chip
                        chip.isCheckable = chip.isChecked
                    }
                } else if (ids.size < 3 || savedList.size < 3) {
                    for (index in 0 until binding.chipGroup.childCount) {
                        val chip = binding.chipGroup.getChildAt(index) as Chip
                        chip.isCheckable = true
                    }
                }
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
               Log.d("태그 들어갓나", "${registerViewModel.fetchDogLikeTag().first()}")
           }
        }
    }

    private fun updateCheckedChip() {
        lifecycleScope.launch {
            if (registerViewModel.fetchDogLikeTag().first().isNotEmpty()) {
                val tagList: List<String> = registerViewModel.fetchDogLikeTag().first().split(",")
                Log.d("리스트0번", tagList[0])
                for (index in 0 until binding.chipGroup.childCount) {
                    val chip = binding.chipGroup.getChildAt(index) as Chip
                    if (tagList.contains(chip.text)) {
                        chip.isChecked = true
                        Log.d("업데이트 칩아이디", "${chip.id}")
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
