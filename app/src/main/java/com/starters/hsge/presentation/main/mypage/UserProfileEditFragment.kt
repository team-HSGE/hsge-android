package com.starters.hsge.presentation.main.mypage

import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserProfileEditBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity

class UserProfileEditFragment:BaseFragment<FragmentUserProfileEditBinding>(R.layout.fragment_user_profile_edit) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBackBtn.setOnClickListener{
            visibleBtmNav()
        }

    }

    fun visibleBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }

}