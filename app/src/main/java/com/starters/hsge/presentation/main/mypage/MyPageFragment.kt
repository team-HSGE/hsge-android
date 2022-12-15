package com.starters.hsge.presentation.main.mypage

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentMyPageBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivSettings.setOnClickListener{
            findNavController().navigate(R.id.action_myPageFragment_to_settingsFragment)
            goneBtmNav()
        }

        binding.userProfileEditSection.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_userProfileEditFragment)
            goneBtmNav()
        }

        binding.locationSettingSection.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_userLocationFragment2)
            prefs.edit().putInt("getLocationFrom", 1).apply()
            goneBtmNav()
        }

        binding.radiusSettingSection.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_userDistanceFragment)
            goneBtmNav()
        }

        binding.dogProfileManageSection.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_managementFragment)
            goneBtmNav()
        }
    }

    private fun goneBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.GONE
    }

}