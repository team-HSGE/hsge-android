package com.starters.hsge.presentation.register

import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogSexBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class DogSexFragment : BaseFragment<FragmentDogSexBinding>(R.layout.fragment_dog_sex) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {

        binding.btnMale.setOnClickListener {
            if (binding.btnMale.isSelected) {
                !binding.btnMale.isSelected
            }
        }

        binding.btnFemale.setOnClickListener {

        }
    }
}