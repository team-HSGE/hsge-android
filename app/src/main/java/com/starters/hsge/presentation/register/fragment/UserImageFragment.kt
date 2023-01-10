package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserImageBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.constants.SAVE_RESID_FOR_VIEW
import com.starters.hsge.presentation.common.constants.SAVE_RESID_ORDER
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserImageFragment : BaseFragment<FragmentUserImageBinding>(R.layout.fragment_user_image) {

    var resId: Int? = null
    var resIdForView: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        getSharedPreferences()
        updateUserIcon()
        setNavigation()
        changeButtonState()
    }

    private fun changeButtonState() {
        if (prefs.contains(SAVE_RESID_ORDER)) {
            binding.btnNext.isEnabled = true
        }
    }

    private fun updateUserIcon() {
        if (resIdForView != 0) {
            resIdForView?.let { binding.ivUserImage.setImageResource(it) }
        }
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_userImageFragment_to_dogPhotoFragment)
        }

        binding.ivUserImage.setOnClickListener {
            val action =
                UserImageFragmentDirections.actionUserImageFragmentToProfileIconFragment(1, "")
            findNavController().navigate(action)
        }
    }

    private fun getSharedPreferences() {
        resId = prefs.getInt(SAVE_RESID_ORDER, R.drawable.ic_profile_photo_bg)
        resIdForView = prefs.getInt(SAVE_RESID_FOR_VIEW, 0)
        Timber.d("선택한 이미지: ${resIdForView}, 매핑: $resId")
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}