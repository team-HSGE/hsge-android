package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.UserInfoPutRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface UserInfoPutApi {
    @PUT("api/users")
    fun putUserInfo(@Body changeUserInfo: UserInfoPutRequest) : Call<Void>

}