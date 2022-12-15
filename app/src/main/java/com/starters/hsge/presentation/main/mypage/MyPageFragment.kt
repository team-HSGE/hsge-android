package com.starters.hsge.presentation.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentMyPageBinding
import com.starters.hsge.network.*
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 사용자 정보 조회
        tryGetUsersInfo()

        binding.ivSettings.setOnClickListener{
            findNavController().navigate(R.id.action_myPageFragment_to_settingsFragment)
            goneBtmNav()
        }

        binding.userProfileEditSection.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_userProfileEditFragment)
            goneBtmNav()
        }

        binding.locationSettingSection.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_userLocationFragment2)
            prefs.edit().putInt("getLocationFrom", 1).apply()
            goneBtmNav()
        }

        binding.radiusSettingSection.setOnClickListener {
            findNavController().navigate(R.id.action_myPageFragment_to_userDistanceFragment)
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
        val usersGetInterface = RetrofitClient.sRetrofit.create(UsersGetInterface::class.java)
        usersGetInterface.getUsersInfo().enqueue(object :
            Callback<UsersGetResponse> {
            override fun onResponse(
                call: Call<UsersGetResponse>,
                response: Response<UsersGetResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body() as UsersGetResponse

                    // 사용자 정보 가져오기
                    var profileImage = result.profilePath
                    var nickname = result.nickname
                    var town = result.town
                    var latitude = result.latitude
                    var longitude = result.longtitude

                    binding.ivUserProfile.setImageResource(profileImage)
                    binding.tvUserNickname.text = nickname
                    binding.tvUserLocation.text = town

                } else  {
                    when(response.code()) {

                    }
                }
            }

            override fun onFailure(call: Call<UsersGetResponse>, t: Throwable) {
                Log.d("실패", t.message ?: "통신오류")

            }
        })
    }

}