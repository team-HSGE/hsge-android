package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.LoginRequest
import com.starters.hsge.data.model.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("api/auth/login")
    fun postLogin(@Body accessToken: LoginRequest) : Call<LoginResponse>
}