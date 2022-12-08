package com.starters.hsge.presentation.register.fragment.UserLocationFragment

import com.starters.hsge.data.model.userLocation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface LocationService {
    @PUT("/api/users/geolocation")
    fun putLocationData(@Body request: userLocation) : Call<Void>
}