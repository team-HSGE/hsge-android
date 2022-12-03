package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.DogBreedResponse
import retrofit2.http.GET

interface DogOptionApi {
    @GET("")
    suspend fun getBreed(): DogBreedResponse
}