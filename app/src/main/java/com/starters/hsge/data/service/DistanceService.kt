package com.starters.hsge.data.service

import com.starters.hsge.data.api.DistanceApi
import com.starters.hsge.data.interfaces.distanceInterface
import com.starters.hsge.data.model.remote.request.DistanceRequest
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Response


class DistanceService(val distanceInterface: distanceInterface) {

    fun tryPostDistance(distance: Double){
        val distanceApi= RetrofitClient.sRetrofit.create(DistanceApi::class.java)
        val radius = distance / 100

        distanceApi.putDistanceData(DistanceRequest(radius)).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                distanceInterface.onPostDistanceSuccess(response.isSuccessful, response.code())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                distanceInterface.onPostIsLikeFailure(t.message ?: "통신 오류")
            }
        })
    }
}