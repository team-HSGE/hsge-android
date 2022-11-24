package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AccessTokenInterface {

    @POST("api/users")
    fun postLogin(@Body params: String) : Call<ATResponse>
}