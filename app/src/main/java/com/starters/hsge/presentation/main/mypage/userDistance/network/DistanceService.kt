package com.starters.hsge.presentation.main.mypage.userDistance.network

import com.starters.hsge.data.model.remote.request.DistanceRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface DistanceService {
    @PUT("/api/users/radius")
    fun putDistanceData(@Body request: DistanceRequest) : Call<Void>
}