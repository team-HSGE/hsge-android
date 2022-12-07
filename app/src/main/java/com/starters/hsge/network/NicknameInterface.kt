package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface NicknameInterface {

    @POST("api/auth/duplicate-nickname")
    fun postNickname(@Body nickname: NicknameRequest) : Call<NicknameResponse>
}