package com.starters.hsge.data.service

import com.starters.hsge.data.api.UserInfoPutApi
import com.starters.hsge.data.interfaces.UserInfoPutInterface
import com.starters.hsge.data.model.remote.request.UserInfoPutRequest
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoPutService(val userInfoPutInterface: UserInfoPutInterface) {

    fun tryPutUserInfo(userInfo: UserInfoPutRequest) {
        val userInfoPutApi = RetrofitClient.sRetrofit.create(UserInfoPutApi::class.java)
        userInfoPutApi.putUserInfo(userInfo).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                userInfoPutInterface.onPutUserInfoSuccess(response.isSuccessful, response.code())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                userInfoPutInterface.onPutUserInfoFailure(t.message ?: "통신 오류")
            }
        })
    }
}