package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserProfileIconBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.adapter.UserProfileIconAdapter

class UserProfileIconFragment : BaseFragment<FragmentUserProfileIconBinding>(R.layout.fragment_user_profile_icon) {

    private lateinit var adapter: UserProfileIconAdapter
    private val args : UserProfileIconFragmentArgs by navArgs()
    private lateinit var action : NavDirections

    var userIconList = listOf<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userIconList = listOf<Int>(
            R.drawable.ic_dog_profile_1,
            R.drawable.ic_dog_profile_6,
            R.drawable.ic_dog_profile_11,
            R.drawable.ic_dog_profile_2,
            R.drawable.ic_dog_profile_7,
            R.drawable.ic_dog_profile_12,
            R.drawable.ic_dog_profile_3,
            R.drawable.ic_dog_profile_8,
            R.drawable.ic_dog_profile_13,
            R.drawable.ic_dog_profile_4,
            R.drawable.ic_dog_profile_9,
            R.drawable.ic_dog_profile_14,
            R.drawable.ic_dog_profile_5,
            R.drawable.ic_dog_profile_10,
            R.drawable.ic_dog_profile_15
        )

        setNavigation()
        initRecyclerView(userIconList)
    }

    private fun initRecyclerView(list: List<Int>) {
        adapter = UserProfileIconAdapter(list)
        binding.rvProfileIcon.adapter = adapter

        adapter.setItemClickListener(object: UserProfileIconAdapter.OnItemClickListener{
            override fun onClick(v: View, resId: Int) {
                if(args.checkLayout == 1) {
                    // 회원가입
                    prefs.edit().putInt("resId", resId).apply()
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_userProfileIconFragment_to_userImageFragment)

                } else if (args.checkLayout == 2) {
                    // 회원 정보 수정
                    action = UserProfileIconFragmentDirections.actionUserProfileEditIconFragmentToUserProfileEditFragment(null, resId)
                    findNavController().navigate(action)
                }
            }
        })
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}