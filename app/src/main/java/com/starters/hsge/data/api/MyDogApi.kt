package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.response.MyDogResponse
import retrofit2.http.GET

interface MyDogApi {
    @GET("/api/pets")
    suspend fun getMyDog(): List<MyDogResponse>
}