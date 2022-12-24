package com.starters.hsge.presentation.main.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentChatPartnerSpecificDogBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class SpecificDogFragment : BaseFragment<FragmentChatPartnerSpecificDogBinding>(R.layout.fragment_chat_partner_specific_dog){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}