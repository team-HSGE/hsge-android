package com.starters.hsge.presentation.register.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserProfileIconBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.login.LoginActivity
import com.starters.hsge.presentation.register.adapter.UserProfileIconAdapter

class UserProfileIconFragment : BaseFragment<FragmentUserProfileIconBinding>(R.layout.fragment_user_profile_icon) {

    private lateinit var adapter: UserProfileIconAdapter

    var userIconList = listOf<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userIconList = listOf<Int>(
            R.drawable.ic_profile_icon_1,
            R.drawable.ic_profile_icon_2,
            R.drawable.ic_profile_icon_3,
            R.drawable.ic_profile_icon_4,
            R.drawable.ic_profile_icon_5,
            R.drawable.ic_profile_icon_6,
            R.drawable.ic_profile_icon_7,
            R.drawable.ic_profile_icon_8
        )

        setNavigation()
        initRecyclerView(userIconList)

    }

    private fun initRecyclerView(list: List<Int>) {
        adapter = UserProfileIconAdapter(list)
        binding.rvProfileIcon.adapter = adapter
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}