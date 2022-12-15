package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.GET

interface UsersGetInterface {
    @GET("api/users")
    fun getUsersInfo() : Call<UsersGetResponse>
}