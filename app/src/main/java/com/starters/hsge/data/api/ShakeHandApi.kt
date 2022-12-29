package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.CurrentLocationPostRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ShakeHandApi {
    @POST("api/users/currentLocation")
    fun postCurrentLocation(
        @Body currentLocation: CurrentLocationPostRequest
    ): Call<Void>
}