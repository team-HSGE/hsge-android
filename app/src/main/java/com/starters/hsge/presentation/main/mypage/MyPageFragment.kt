package com.starters.hsge.presentation.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.UserInfoGetInterface
import com.starters.hsge.data.model.remote.response.UserInfoGetResponse
import com.starters.hsge.data.service.UserInfoGetService
import com.starters.hsge.databinding.FragmentMyPageBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page), UserInfoGetInterface {

    var profileImage : Int = 0
    var nickname : String = ""
    var town : String = ""
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    var splitTown : String = ""
    var radius : Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 사용자 정보 조회
        UserInfoGetService(this).tryGetUserInfo()

        binding.ivSettings.setOnClickListener{
            findNavController().navigate(R.id.action_myPageFragment_to_settingsFragment)
            goneBtmNav()
        }

        binding.userProfileEditSection.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToUserProfileEditFragment(
                UserInfoData(profileImage, nickname)
            )
            findNavController().navigate(action)
            goneBtmNav()
        }

        binding.locationSettingSection.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToEditLocationFragment(
                UserLocationData(town, latitude, longitude, radius)
            )
            findNavController().navigate(action)
            goneBtmNav()
        }

        binding.radiusSettingSection.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageFragmentToUserDistanceFragment(
                UserLocationData(town, latitude, longitude, radius)
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

        binding.appInfoSection.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_appInfoFragment)
            goneBtmNav()
        }
    }

    private fun goneBtmNav(){ (activity as MainActivity).binding.navigationMain.visibility = View.GONE }

    override fun onGetUserInfoSuccess(userInfoGetResponse: UserInfoGetResponse, isSuccess: Boolean, code: Int) {
        if (isSuccess) {
            // 사용자 정보 가져오기
            profileImage = userInfoGetResponse.profilePath
            nickname = userInfoGetResponse.nickname
            town = userInfoGetResponse.town
            latitude = userInfoGetResponse.latitude
            longitude = userInfoGetResponse.longtitude
            radius = userInfoGetResponse.radius

            var townStrList =  town.split(" ")
            if (townStrList.size == 3) {
                splitTown = townStrList[1] + " " + townStrList[2]
            } else if (townStrList.size > 3) {
                splitTown = townStrList[1] + " " + townStrList[2] + " " + townStrList[3]
            }

            // 사용자 정보 적용
            binding.ivUserProfile.setBackgroundResource(profileImage)
            binding.tvUserNickname.text = nickname
            binding.tvUserLocation.text = splitTown
        }
    }

    override fun onGetUserInfoFailure(message: String) {
        Log.d("UserInfoGet 오류", "오류: $message")
    }
}