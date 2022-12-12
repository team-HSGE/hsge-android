package com.starters.hsge.presentation.main.mypage.userDistance.network

import com.starters.hsge.data.model.Distance
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface DistanceService {
    @PUT("/api/users/radius")
    fun putDistanceData(@Body request: Distance) : Call<Void>
}