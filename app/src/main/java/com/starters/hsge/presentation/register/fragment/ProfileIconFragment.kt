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
import com.starters.hsge.domain.usecase.GetIconUseCase
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.constants.SAVE_RESID_FOR_VIEW
import com.starters.hsge.presentation.common.constants.SAVE_RESID_ORDER
import com.starters.hsge.presentation.register.adapter.UserProfileIconAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileIconFragment :
    BaseFragment<FragmentProfileIconBinding>(R.layout.fragment_profile_icon) {

    val REGISTER_SCREEN = 1
    val USER_PROFILE_SCREEN = 2

    @Inject
    lateinit var getIconUseCase: GetIconUseCase
    private lateinit var adapter: UserProfileIconAdapter

    private val args: ProfileIconFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()
        initRecyclerView(getIconUseCase.invoke())
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
        })
        binding.rvProfileIcon.adapter = adapter
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
