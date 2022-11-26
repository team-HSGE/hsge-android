package com.starters.hsge.presentation.register

import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserProfileIconBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.adapter.UserProfileIconAdapter

class UserProfileIconFragment : BaseFragment<FragmentUserProfileIconBinding>(R.layout.fragment_user_profile_icon) {

    private lateinit var adapter: UserProfileIconAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userIconList = listOf<Int>(
            R.drawable.ic_profile_icon_1,
            R.drawable.ic_profile_icon_2,
            R.drawable.ic_profile_icon_3,
            R.drawable.ic_profile_icon_4,
            R.drawable.ic_profile_icon_5,
            R.drawable.ic_profile_icon_6,
            R.drawable.ic_profile_icon_7,
            R.drawable.ic_profile_icon_8
        )

        initRecyclerView(userIconList)
    }

    private fun initRecyclerView(list: List<Int>) {
        adapter = UserProfileIconAdapter(list)
        binding.rvProfileIcon.adapter = adapter
    }
}