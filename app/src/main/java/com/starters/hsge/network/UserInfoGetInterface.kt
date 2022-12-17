package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.GET

interface UserInfoGetInterface {
    @GET("api/users")
    fun getUserInfo() : Call<UserInfoGetResponse>
}