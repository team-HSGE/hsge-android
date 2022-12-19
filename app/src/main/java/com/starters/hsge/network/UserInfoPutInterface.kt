package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface UserInfoPutInterface {
    @PUT("api/users")
    fun putUserInfo(@Body changeUserInfo: UserInfoPutRequest) : Call<Void>

}