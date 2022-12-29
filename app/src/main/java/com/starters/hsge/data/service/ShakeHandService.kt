package com.starters.hsge.data.service

import com.starters.hsge.data.api.ShakeHandApi
import com.starters.hsge.data.interfaces.ShakeHandInterface
import com.starters.hsge.data.model.remote.request.CurrentLocationPostRequest
import com.starters.hsge.data.model.remote.response.ShakeHandResponse
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShakeHandService(val shakeHandInterface: ShakeHandInterface) {

    fun tryPostCurrentLocation(currentLocation: CurrentLocationPostRequest) {
        val shakeHandApi = RetrofitClient.sRetrofit.create(ShakeHandApi::class.java)
        shakeHandApi.postCurrentLocation(currentLocation).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                shakeHandInterface.onPostCurrentLocationSuccess(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                shakeHandInterface.onPostCurrentLocationFailure(t.message ?: "통신 오류")
            }
        })
    }

    fun tryGetHandShake() {
        val shakeHandApi = RetrofitClient.sRetrofit.create(ShakeHandApi::class.java)
        shakeHandApi.getShakeHand().enqueue(object :
            Callback<List<ShakeHandResponse>> {
            override fun onResponse(
                call: Call<List<ShakeHandResponse>>,
                response: Response<List<ShakeHandResponse>>
            ) {
                shakeHandInterface.onGetShakeHandSuccess(response.body() as List<ShakeHandResponse>, response.isSuccessful)
            }

            override fun onFailure(call: Call<List<ShakeHandResponse>>, t: Throwable) {
                shakeHandInterface.onGetShakeHandFailure(t.message ?: "통신 오류")
            }
        })
    }
}