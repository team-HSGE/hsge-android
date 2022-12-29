package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.response.MyDogResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PartnerApi {
    @GET("/api/users/{userId}/pets")
    suspend fun getPartnerDogs(
        @Path("userId") userId: Long
    ): List<MyDogResponse>
}