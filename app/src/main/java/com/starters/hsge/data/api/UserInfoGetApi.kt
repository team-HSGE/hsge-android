package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.response.UserInfoGetResponse
import retrofit2.Call
import retrofit2.http.GET

interface UserInfoGetApi {
    @GET("api/users")
    fun getUserInfo() : Call<UserInfoGetResponse>
}