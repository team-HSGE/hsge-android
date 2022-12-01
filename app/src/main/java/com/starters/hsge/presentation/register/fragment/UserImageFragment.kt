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
    var position : Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        if (prefs.contains("resId")){
            setUserProfile()
        } else {
            return
        }
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userImageFragment_to_dogPhotoFragment)
            prefs.edit().remove("position").commit()
        }

        binding.ivUserImage.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userImageFragment_to_userProfileIconFragment)
            prefs.edit().remove("position").commit()
            prefs.edit().remove("resId").commit()
        }
    }

    private fun setUserProfile(){
        getSharedPreferences()

        when(position) {
            0 -> binding.ivUserImage.setImageResource(R.drawable.ic_profile_icon_1)
            1 -> binding.ivUserImage.setImageResource(R.drawable.ic_profile_icon_2)
            2 -> binding.ivUserImage.setImageResource(R.drawable.ic_profile_icon_3)
            3 -> binding.ivUserImage.setImageResource(R.drawable.ic_profile_icon_4)
            4 -> binding.ivUserImage.setImageResource(R.drawable.ic_profile_icon_5)
            5 -> binding.ivUserImage.setImageResource(R.drawable.ic_profile_icon_6)
            6 -> binding.ivUserImage.setImageResource(R.drawable.ic_profile_icon_7)
            7 -> binding.ivUserImage.setImageResource(R.drawable.ic_profile_icon_8)
        }
    }

    private fun getSharedPreferences() {
        resId = prefs.getInt("resId", 0) //212312
        position = prefs.getInt("position", 0) // 0
        Log.d("sp", "${resId}")
    }

}