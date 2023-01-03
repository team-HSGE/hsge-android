package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.IsLikeRequest
import com.starters.hsge.data.model.remote.response.DogCard
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HomeApi {
    @GET("/api/pets/area")
    fun getDogData(): Call<List<DogCard>>

    @POST("/api/pets/{petId}/interest")
    fun postIsLikeData(
        @Path("petId") petId: Int,
        @Body like: IsLikeRequest
    ): Call<Void>
}