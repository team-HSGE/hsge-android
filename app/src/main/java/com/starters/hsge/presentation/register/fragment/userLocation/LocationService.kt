package com.starters.hsge.presentation.register.fragment.userLocation

import com.starters.hsge.data.model.remote.request.userLocationRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface LocationService {
    @PUT("/api/users/geolocation")
    fun putLocationData(@Body request: userLocationRequest) : Call<Void>
}