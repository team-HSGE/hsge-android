package com.starters.hsge.presentation.main.partner

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentChatPartnerDogsBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class PartnerDogsFragment: BaseFragment<FragmentChatPartnerDogsBinding>(R.layout.fragment_chat_partner_dogs) {

    private val partnerDogsList = listOf(
        PartnerDogs(
            img = null,
            name = "홍삼",
            sex = null,
            age = "3살",
            breed = "슈나우저"
        ),
        PartnerDogs(
            img = null,
            name = "캔디",
            sex = null,
            age = "2살",
            breed = "이탈리안 그레이하운드"
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setNavigation()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initRecyclerView() {
        val adapter = PartnerDogsAdapter(partnerDogsList)
        binding.rvChatPartnerDogsList.layoutManager = LinearLayoutManager(context)
        binding.rvChatPartnerDogsList.adapter = adapter
    }
}

data class PartnerDogs(
    val img: String?,
    val name: String,
    val sex: Int?,
    val age: String,
    val breed: String
)