package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.UserLocationRequest
import retrofit2.http.Body
import retrofit2.http.PUT

interface EditLocationApi {
    @PUT("/api/users/geolocation")
    suspend fun putLocationData(
        @Body location: UserLocationRequest
    )
}