package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.FcmPostRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmPostApi {
    @POST("api/auth/fcm/token")
    fun postFcmToken(@Body token: FcmPostRequest) : Call<Void>
}