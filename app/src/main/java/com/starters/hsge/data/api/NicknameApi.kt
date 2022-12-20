package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.NicknameRequest
import com.starters.hsge.data.model.remote.response.NicknameResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NicknameApi {

    @POST("api/auth/duplicate-nickname")
    fun postNickname(@Body nickname: NicknameRequest) : Call<NicknameResponse>
}