package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AccessTokenInterface {

    @POST("login")
    fun postLogin(@Body accessToken: AccessRequest) : Call<AccessResponse>
}