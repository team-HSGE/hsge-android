package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmPostInterface {

    @POST("api/auth/fcm/token")
    fun postFcmToken(@Body token: FcmPostRequest) : Call<Void>
}