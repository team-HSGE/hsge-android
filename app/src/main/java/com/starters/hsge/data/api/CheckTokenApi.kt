package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.CheckTokenRequest
import com.starters.hsge.data.model.remote.response.CheckTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckTokenApi {

    @POST("api/auth/refresh-token")
    fun postToken(@Body checkToken: CheckTokenRequest
    ) : Call<CheckTokenResponse?>
}