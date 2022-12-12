package com.starters.hsge.presentation.main.home.network

import com.starters.hsge.data.model.IsLikeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IsLikeService {
    @POST("/api/")
    fun postIsLikeData(@Body request: IsLikeRequest) : Call<Void>
}