package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.UserLocationRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface LocationApi {
    @PUT("/api/users/geolocation")
    fun putLocationData(@Body request: UserLocationRequest) : Call<Void>
}