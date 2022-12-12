package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NicknameInterface {

    @POST("api/auth/duplicate-nickname")
    fun postNickname(@Body nickname: NicknameRequest) : Call<NicknameResponse>
}