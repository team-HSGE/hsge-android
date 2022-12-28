package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.response.DogCard
import retrofit2.Call
import retrofit2.http.GET

interface HomeDogApi {
    @GET("/api/pets/area")
     fun getDogData(): Call<List<DogCard>>
}