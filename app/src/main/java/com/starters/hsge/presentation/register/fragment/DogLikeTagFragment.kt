package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogLikeTagBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class DogLikeTagFragment : BaseFragment<FragmentDogLikeTagBinding>(R.layout.fragment_dog_like_tag) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        // chip click listener
        binding.chipMan.setOnCheckedChangeListener { chip, isChecked ->
            if (!isChecked) {
                chip.text
            } else {

            }
        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogLikeTagFragment_to_dogDislikeTagFragment)
        }
    }
}