package com.starters.hsge.presentation.main.partner

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentChatPartnerDogsBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PartnerDogsFragment :
    BaseFragment<FragmentChatPartnerDogsBinding>(R.layout.fragment_chat_partner_dogs) {

    private val partnerDogsViewModel: PartnerDogsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setUpToolbar()
        setNavigation()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initRecyclerView() {
        partnerDogsViewModel.getPartnerDogs(requireArguments().getLong("partnerId"))
            .observe(viewLifecycleOwner) {
                val adapter = it?.let { it1 -> PartnerDogsAdapter(it1) }
                binding.rvChatPartnerDogsList.adapter = adapter
            }
    }

    private fun setUpToolbar() {
        binding.tvChatPartnerNicknameTitle.text = requireArguments().getString("partnerNickName")
    }
}