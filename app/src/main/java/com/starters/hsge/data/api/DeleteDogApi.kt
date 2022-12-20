package com.starters.hsge.data.api

import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteDogApi {
    @DELETE("/api/pets/{petId}")
    suspend fun deleteDog(
        @Path("petId") petId: Int
    )
}