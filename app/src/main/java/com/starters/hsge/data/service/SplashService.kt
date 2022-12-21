package com.starters.hsge.data.service

import com.starters.hsge.data.api.CheckTokenApi
import com.starters.hsge.data.interfaces.SplashInterface
import com.starters.hsge.data.model.remote.request.CheckTokenRequest
import com.starters.hsge.data.model.remote.response.CheckTokenResponse
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashService(val splashInterface: SplashInterface) {

    fun tryPostCheckToken(checkToken: CheckTokenRequest) {
        val checkTokenApi = RetrofitClient.sRetrofit.create(CheckTokenApi::class.java)
        checkTokenApi.postToken(checkToken).enqueue(object :
            Callback<CheckTokenResponse?> {
            override fun onResponse(
                call: Call<CheckTokenResponse?>,
                response: Response<CheckTokenResponse?>
            ) {
                splashInterface.onPostCheckTokenSuccess(response.body() as CheckTokenResponse, response.isSuccessful, response.code())
            }

            override fun onFailure(call: Call<CheckTokenResponse?>, t: Throwable) {
                splashInterface.onPostCheckTokenFailure(t.message ?: "통신 오류")

            }
        })
    }
}