package com.starters.hsge.data.service

import com.starters.hsge.data.api.LoginApi
import com.starters.hsge.data.interfaces.SplashInterface
import com.starters.hsge.data.model.remote.request.CheckTokenRequest
import com.starters.hsge.data.model.remote.response.CheckTokenResponse
import com.starters.hsge.domain.util.ErrorConvertUtil
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashService(val splashInterface: SplashInterface) {

    fun tryPostCheckToken(checkToken: CheckTokenRequest) {
        val checkTokenApi = RetrofitClient.sRetrofit.create(LoginApi::class.java)
        checkTokenApi.postToken(checkToken).enqueue(object :
            Callback<CheckTokenResponse?> {
            override fun onResponse(
                call: Call<CheckTokenResponse?>,
                response: Response<CheckTokenResponse?>
            ) {
                if (response.isSuccessful) {
                    splashInterface.onPostCheckTokenSuccess(response.body() as CheckTokenResponse?, response.isSuccessful,
                        response.code(), error = null)
                } else {
                    val errorBody = ErrorConvertUtil.getErrorResponse(response.errorBody()!!)
                    splashInterface.onPostCheckTokenSuccess(response.body() as CheckTokenResponse?, response.isSuccessful,
                        response.code(), errorBody.message)
                }
            }

            override fun onFailure(call: Call<CheckTokenResponse?>, t: Throwable) {
                splashInterface.onPostCheckTokenFailure(t.message ?: "통신 오류")
            }
        })
    }
}