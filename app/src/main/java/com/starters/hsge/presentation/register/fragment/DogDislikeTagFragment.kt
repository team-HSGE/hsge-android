package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogDislikeTagBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class DogDislikeTagFragment : BaseFragment<FragmentDogDislikeTagBinding>(R.layout.fragment_dog_dislike_tag) {

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
    }
}