package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.IsLikeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface IsLikeApi {
    @POST("/api/pets/{petId}/interest")
    fun postIsLikeData(@Path("petId") petId: Int,
                       @Body request: IsLikeRequest ) : Call<Void>
}