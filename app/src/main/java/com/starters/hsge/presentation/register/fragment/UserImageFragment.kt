package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserImageBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserImageFragment : BaseFragment<FragmentUserImageBinding>(R.layout.fragment_user_image) {

    var resId: Int? = null
    var resIdForView: Int? = null

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        getSharedPreferences()
        updateUserIcon()
        setNavigation()
        changeButtonState()
    }

    private fun changeButtonState() {
        if (prefs.contains("resId")) {
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
                UserImageFragmentDirections.actionUserImageFragmentToUserProfileIconFragment(1)
            findNavController().navigate(action)
        }
    }

    private fun getSharedPreferences() {
        resId = prefs.getInt("resId", R.drawable.ic_profile_photo_bg)
        resIdForView = prefs.getInt("resIdForView", 0)
        Timber.d("선택한 이미지: ${resIdForView}, 매핑: $resId")
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}