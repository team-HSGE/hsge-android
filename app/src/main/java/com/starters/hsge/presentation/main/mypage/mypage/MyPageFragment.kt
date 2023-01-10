package com.starters.hsge.presentation.main.mypage.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentMyPageBinding
import com.starters.hsge.domain.model.LocationInfo
import com.starters.hsge.domain.model.UserProfileInfo
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.constants.OLD_NICKNAME
import com.starters.hsge.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserInfo()
        observeUserInfo()
        initListener()
    }

    private fun getUserInfo() {
        myPageViewModel.getUserInfo()
    }

    private fun observeUserInfo() {
        Timber.d("옵저빙")
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                myPageViewModel.userInfo.collectLatest { state ->
                    when (state) {
                        is MyPageState.Loading -> {
                            //TODO: 로딩 다이얼로그 띄우기
                            Timber.d("!!Loading")
                        }
                        is MyPageState.Failure -> {
                            //TODO: 로딩 다이얼로그 해제
                            Timber.d("!!Failure")
                        }
                        is MyPageState.Success -> {
                            Timber.d("!!Success")
                            binding.userInfo = state.data
                            with(myPageViewModel) {
                                nickName = state.data.nickname
                                userIcon = state.data.profilePath
                                latitude = state.data.latitude
                                longitude = state.data.longtitude
                                town = state.data.town
                                radius = state.data.radius
                            }
                        }
                        is MyPageState.Initial -> {
                            Timber.d("!!Initial")
                        }
                    }
                }
            }
        }
    }

    private fun initListener() {
        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_settingsFragment)
            goneBtmNav()
        }

        binding.userProfileEditSection.setOnClickListener {
            // SharedPreferences에 닉네임 저장
            prefs.edit().putString(OLD_NICKNAME, myPageViewModel.nickName).apply()
            val action = MyPageFragmentDirections.actionMyPageFragmentToUserProfileEditFragment(
                UserProfileInfo(
                    nickName = myPageViewModel.nickName,
                    userIcon = myPageViewModel.userIcon
                )
            )
            findNavController().navigate(action)
            goneBtmNav()
        }

        binding.locationSettingSection.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToEditLocationFragment(
                LocationInfo(
                    latitude = myPageViewModel.latitude,
                    longitude = myPageViewModel.longitude,
                    town = myPageViewModel.town,
                    radius = myPageViewModel.radius
                )
            )
            findNavController().navigate(action)
            goneBtmNav()
        }

        binding.radiusSettingSection.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToUserDistanceFragment(
                LocationInfo(
                    latitude = myPageViewModel.latitude,
                    longitude = myPageViewModel.longitude,
                    town = myPageViewModel.town,
                    radius = myPageViewModel.radius
                )
            )
            findNavController().navigate(action)
            goneBtmNav()
        }

        binding.dogProfileManageSection.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_managementFragment)
            goneBtmNav()
        }

        binding.personalInquireSection.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_inquiryFragment)
            goneBtmNav()
        }
    }

    private fun goneBtmNav() {
        (activity as MainActivity).binding.navigationMain.visibility = View.GONE
    }
}