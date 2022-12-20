package com.starters.hsge.data.service

import com.starters.hsge.data.api.UserInfoGetApi
import com.starters.hsge.data.interfaces.UserInfoGetInterface
import com.starters.hsge.data.model.remote.response.UserInfoGetResponse
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoGetService(val userInfoGetInterface: UserInfoGetInterface) {

    fun tryGetUserInfo() {
        val userInfoGetApi = RetrofitClient.sRetrofit.create(UserInfoGetApi::class.java)
        userInfoGetApi.getUserInfo().enqueue(object :
            Callback<UserInfoGetResponse> {
            override fun onResponse(
                call: Call<UserInfoGetResponse>,
                response: Response<UserInfoGetResponse>
            ) {
                userInfoGetInterface.onGetUserInfoSuccess(response.body() as UserInfoGetResponse, response.isSuccessful, response.code())
            }

            override fun onFailure(call: Call<UserInfoGetResponse>, t: Throwable) {
                userInfoGetInterface.onGetUserInfoFailure(t.message ?: "통신 오류")
            }

        })
    }
}