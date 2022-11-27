package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserImageBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class UserImageFragment : BaseFragment<FragmentUserImageBinding>(R.layout.fragment_user_image) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userImageFragment_to_dogPhotoFragment)
        }

        binding.ivUserImage.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userImageFragment_to_userProfileIconFragment)
        }
    }
}