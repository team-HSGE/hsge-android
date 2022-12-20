package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.DistanceRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface DistanceApi {
    @PUT("/api/users/radius")
    fun putDistanceData(@Body request: DistanceRequest) : Call<Void>
}