package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckTokenInterface {

    @POST("api/auth/refresh-token")
    fun postToken(@Body
                  accessToken: CheckTokenRequest,
                  refreshToken: CheckTokenRequest
    ) : Call<CheckTokenResponse>
}