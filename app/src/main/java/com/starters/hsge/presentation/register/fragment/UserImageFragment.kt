package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserImageBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class UserImageFragment : BaseFragment<FragmentUserImageBinding>(R.layout.fragment_user_image) {

    var resId : Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        getSharedPreferences()
        if (prefs.contains("resId")){
            binding.ivUserImage.setImageResource(resId!!)
        } else {
            return
        }
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

    private fun getSharedPreferences() {
        resId = prefs.getInt("resId", 0)
        Log.d("sp", "${resId}")
    }

}