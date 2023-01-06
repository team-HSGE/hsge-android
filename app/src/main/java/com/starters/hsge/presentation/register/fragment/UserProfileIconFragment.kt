package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.starters.hsge.R
import com.starters.hsge.common.constants.UserIcon
import com.starters.hsge.databinding.FragmentUserProfileIconBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.UserIconFormat.applyUserIconToInt
import com.starters.hsge.presentation.register.adapter.UserProfileIconAdapter
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileIconFragment :
    BaseFragment<FragmentUserProfileIconBinding>(R.layout.fragment_user_profile_icon) {

    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var adapter: UserProfileIconAdapter
    private val args: UserProfileIconFragmentArgs by navArgs()
    private lateinit var action: NavDirections

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
        adapter = UserProfileIconAdapter(list, itemClickListener = {
            if (args.checkLayout == 1) { // register 화면
                // SharedPreference 저장
                prefs.edit().putInt("resId", applyUserIconToInt(it)).apply()
                prefs.edit().putInt("resIdForView", it.resId).apply()

                findNavController().navigate(
                    R.id.action_userProfileIconFragment_to_userImageFragment
                )

                /* 데이터 스토어에 저장 시 delay를 주지 않으면 저장이 안되는 문제 있음
                lifecycleScope.launch(Dispatchers.Main) {
                    val job = lifecycleScope.launch(Dispatchers.Main) {
                        registerViewModel.saveUserIcon(applyUserIconToInt(it)) // mapping
                        registerViewModel.saveUserIconForView(it.resId)
                        Timber.d("유저 아이콘 ${registerViewModel.fetchUserIcon().first()}, ${it.resId}")
                    }
                    job.join()
                }
                 */

            } else if (args.checkLayout == 2) { // 유저 정보 수정 화면
                //safe args로 전달
                action =
                    UserProfileIconFragmentDirections.actionUserProfileEditIconFragmentToUserProfileEditFragment(
                        null,
                        it.resId
                    )
                findNavController().navigate(action)
            }
        })
        binding.rvProfileIcon.adapter = adapter
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}