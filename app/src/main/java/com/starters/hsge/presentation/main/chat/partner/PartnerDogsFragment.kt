package com.starters.hsge.presentation.main.chat.partner

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentChatPartnerDogsBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.constants.PARTNER_ID
import com.starters.hsge.presentation.common.constants.PARTNER_NICKNAME
import com.starters.hsge.presentation.common.util.CustomDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PartnerDogsFragment :
    BaseFragment<FragmentChatPartnerDogsBinding>(R.layout.fragment_chat_partner_dogs) {

    private val partnerDogsViewModel: PartnerDogsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        partnerDogsViewModel.partnerNickName =
            requireArguments().getString(PARTNER_NICKNAME).toString()

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
        val customDecoration = CustomDecoration(1f, 0f, Color.parseColor("#EFEFEF"))
        binding.rvChatPartnerDogsList.addItemDecoration(customDecoration)

        partnerDogsViewModel.getPartnerDogs(requireArguments().getLong(PARTNER_ID))
            .observe(viewLifecycleOwner) {
                val adapter =
                    it?.let { it1 -> PartnerDogsAdapter(it1, partnerDogsViewModel.partnerNickName) }
                binding.rvChatPartnerDogsList.adapter = adapter
            }
    }

    private fun setUpToolbar() {
        binding.tvChatPartnerNicknameTitle.text = partnerDogsViewModel.partnerNickName
    }
}