package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.starters.hsge.R
import com.starters.hsge.common.constants.UserIcon
import com.starters.hsge.common.constants.iconToOrder
import com.starters.hsge.databinding.FragmentProfileIconBinding
import com.starters.hsge.domain.model.UserProfileInfo
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.constants.SAVE_RESID_FOR_VIEW
import com.starters.hsge.presentation.common.constants.SAVE_RESID_ORDER
import com.starters.hsge.presentation.register.adapter.UserProfileIconAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileIconFragment :
    BaseFragment<FragmentProfileIconBinding>(R.layout.fragment_profile_icon) {

    val REGISTER_SCREEN = 1
    val USER_PROFILE_SCREEN = 2

    private lateinit var adapter: UserProfileIconAdapter

    private val args: ProfileIconFragmentArgs by navArgs()

    var userIconList = listOf<UserIcon>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userIconList = listOf(
            UserIcon.ICON_FIRST,
            UserIcon.ICON_SECOND,
            UserIcon.ICON_THIRD,
            UserIcon.ICON_FORTH,
            UserIcon.ICON_FIFTH,
            UserIcon.ICON_SIXTH,
            UserIcon.ICON_SEVENTH,
            UserIcon.ICON_EIGHTH,
            UserIcon.ICON_NINTH,
            UserIcon.ICON_TENTH,
            UserIcon.ICON_ELEVENTH,
            UserIcon.ICON_TWELFTH,
            UserIcon.ICON_THIRTEENTH,
            UserIcon.ICON_FOURTEENTH,
            UserIcon.ICON_FIFTEENTH
        )

        setNavigation()
        initRecyclerView(userIconList)
    }

    private fun initRecyclerView(list: List<UserIcon>) {
        adapter = UserProfileIconAdapter(list, itemClickListener = { it ->
            if (args.checkLayout == REGISTER_SCREEN) {
                prefs.edit().putInt(SAVE_RESID_ORDER, it.resId.iconToOrder()).apply()
                prefs.edit().putInt(SAVE_RESID_FOR_VIEW, it.resId).apply()
                findNavController().navigateUp()
            } else if (args.checkLayout == USER_PROFILE_SCREEN) {
                val action =
                    ProfileIconFragmentDirections.actionProfileIconFragment2ToUserProfileEditFragment(
                        UserProfileInfo(
                            nickName = args.nickName,
                            userIcon = it.resId
                        )
                    )
                findNavController().navigate(action)
            }

            /* 데이터 스토어에 저장 시 delay를 주지 않으면 저장이 안되는 문제 있음
            lifecycleScope.launch(Dispatchers.Main) {
                val job = lifecycleScope.launch(Dispatchers.IO) {
                    registerViewModel.saveUserIcon(applyUserIconToInt(it)) // mapping
                    registerViewModel.saveUserIconForView(it.resId)
                    Timber.d("유저 아이콘 ${registerViewModel.fetchUserIcon().first()}, ${it.resId}")
                }
                job.join()
            }
             */
        })
        binding.rvProfileIcon.adapter = adapter
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
