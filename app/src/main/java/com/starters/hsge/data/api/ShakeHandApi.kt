package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.CurrentLocationPostRequest
import com.starters.hsge.data.model.remote.response.ShakeHandResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ShakeHandApi {
    @POST("api/users/currentLocation")
    fun postCurrentLocation(
        @Body currentLocation: CurrentLocationPostRequest
    ): Call<Void>

    @GET("api/users/handShake")
    fun getShakeHand(): Call<List<ShakeHandResponse>>

    @POST("api/user/{userId}/wave")
    fun postShakeHand(@Path ("userId") userId: Long
    ): Call<Void>
}