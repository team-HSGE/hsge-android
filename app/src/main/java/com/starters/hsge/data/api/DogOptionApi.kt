package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.response.DogBreedResponse
import retrofit2.http.GET

interface DogOptionApi {
    @GET("api/common/breed")
    suspend fun getBreed(): DogBreedResponse
}