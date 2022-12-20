package com.starters.hsge.presentation.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.data.api.UserInfoGetApi
import com.starters.hsge.data.model.remote.response.UserInfoGetResponse
import com.starters.hsge.databinding.FragmentMyPageBinding
import com.starters.hsge.network.RetrofitClient
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

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
        tryGetUsersInfo()

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
    }

    private fun goneBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.GONE
    }

    private fun tryGetUsersInfo(){
        val userInfoGetApi = RetrofitClient.sRetrofit.create(UserInfoGetApi::class.java)
        userInfoGetApi.getUserInfo().enqueue(object :
            Callback<UserInfoGetResponse> {
            override fun onResponse(
                call: Call<UserInfoGetResponse>,
                response: Response<UserInfoGetResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body() as UserInfoGetResponse

                    // 사용자 정보 가져오기
                    profileImage = result.profilePath
                    nickname = result.nickname
                    town = result.town
                    latitude = result.latitude
                    longitude = result.longtitude
                    radius = result.radius

                    var townStrList =  town.split(" ")
                    if (townStrList.size == 3) {
                        splitTown = townStrList[1] + " " + townStrList[2]
                    } else if (townStrList.size > 3) {
                        splitTown = townStrList[1] + " " + townStrList[2] + " " + townStrList[3]
                    }

                    // 사용자 정보 적용
                    binding.ivUserProfile.setImageResource(profileImage)
                    binding.tvUserNickname.text = nickname
                    binding.tvUserLocation.text = splitTown
                }
            }

            override fun onFailure(call: Call<UserInfoGetResponse>, t: Throwable) {
                Log.d("MyPage 실패", t.message ?: "통신오류")

            }
        })
    }

}