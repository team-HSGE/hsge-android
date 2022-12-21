package com.starters.hsge.data.service

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.starters.hsge.data.api.FcmPostApi
import com.starters.hsge.data.api.LoginApi
import com.starters.hsge.data.interfaces.LoginInterface
import com.starters.hsge.data.model.remote.request.FcmPostRequest
import com.starters.hsge.data.model.remote.request.LoginRequest
import com.starters.hsge.data.model.remote.response.LoginResponse
import com.starters.hsge.network.RetrofitClient
import com.starters.hsge.presentation.common.base.BaseActivity
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.register.RegisterActivity
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

    fun tryPostFcmToken(fcmToken: FcmPostRequest) {
        val fcmPostApi = RetrofitClient.sRetrofit.create(FcmPostApi::class.java)
        fcmPostApi.postFcmToken(fcmToken).enqueue(object :
        Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                loginInterface.onPostFcmTokenSuccess(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                loginInterface.onPostFcmTokenFailure(t.message ?: "통신 오류")
            }

        })
    }
}