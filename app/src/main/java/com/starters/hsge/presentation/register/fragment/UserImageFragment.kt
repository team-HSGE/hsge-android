package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserImageBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.mypage.MyPageFragmentDirections
import com.starters.hsge.presentation.main.mypage.UserInfoData

class UserImageFragment : BaseFragment<FragmentUserImageBinding>(R.layout.fragment_user_image) {

    var resId : Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        setNavigation()
        getSharedPreferences()
        if (prefs.contains("resId")){
            binding.btnNext.isEnabled = true
            binding.ivUserImage.setImageResource(resId!!)
        } else {
            return
        }
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            if(prefs.contains("resId")) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_userImageFragment_to_dogPhotoFragment)
            } else {
                return@setOnClickListener
            }
        }

        binding.ivUserImage.setOnClickListener {
            val action = UserImageFragmentDirections.actionUserImageFragmentToUserProfileIconFragment(1)
            findNavController().navigate(action)
        }
    }

    private fun getSharedPreferences() {
        resId = prefs.getInt("resId", R.drawable.ic_profile_photo_bg)
        Log.d("sp", "${resId}")
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}