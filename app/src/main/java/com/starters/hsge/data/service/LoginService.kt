package com.starters.hsge.data.service

import com.starters.hsge.data.api.LoginApi
import com.starters.hsge.data.interfaces.LoginInterface
import com.starters.hsge.data.model.remote.request.FcmPostRequest
import com.starters.hsge.data.model.remote.request.LoginRequest
import com.starters.hsge.data.model.remote.response.CheckTokenResponse
import com.starters.hsge.data.model.remote.response.LoginResponse
import com.starters.hsge.domain.util.ErrorConvertUtil
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginService(val loginInterface: LoginInterface) {

    fun tryPostAccessToken(accessToken : LoginRequest) {
        val loginApi = RetrofitClient.sRetrofit.create(LoginApi::class.java)
        loginApi.postLogin(accessToken).enqueue(object :
            Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful) {
                    loginInterface.onPostAccessTokenSuccess(response.body() as LoginResponse?, response.isSuccessful,
                        response.code(), error = null)
                } else {
                    val errorBody = ErrorConvertUtil.getErrorResponse(response.errorBody()!!)
                    loginInterface.onPostAccessTokenSuccess(response.body() as LoginResponse?, response.isSuccessful,
                        response.code(), errorBody?.message)
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                loginInterface.onPostAccessTokenFailure(t.message ?: "통신 오류")
            }
        })
    }

    fun tryPostFcmToken(fcmToken: FcmPostRequest) {
        val fcmPostApi = RetrofitClient.sRetrofit.create(LoginApi::class.java)
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