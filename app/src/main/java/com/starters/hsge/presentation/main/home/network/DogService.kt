package com.starters.hsge.presentation.main.home.network

import com.starters.hsge.data.model.DogCard
import retrofit2.Call
import retrofit2.http.GET

interface DogService {
    @GET("/pets/area")
    fun getDogData() : Call<List<DogCard>>
}