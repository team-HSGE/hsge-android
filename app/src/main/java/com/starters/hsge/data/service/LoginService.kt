package com.starters.hsge.data.service

import com.starters.hsge.data.api.LoginApi
import com.starters.hsge.data.interfaces.LoginInterface
import com.starters.hsge.data.model.remote.request.LoginRequest
import com.starters.hsge.data.model.remote.response.LoginResponse
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginService(val loginInterface: LoginInterface) {

    fun tryPostAccessToken(accessToken : LoginRequest) {
        val loginApi = RetrofitClient.sRetrofit.create(LoginApi::class.java)
        loginApi.postLogin(accessToken).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                loginInterface.onPostAccessTokenSuccess(response.body() as LoginResponse, response.isSuccessful, response.code())
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginInterface.onPostAccessTokenFailure(t.message ?: "통신 오류")
            }
        })
    }
}