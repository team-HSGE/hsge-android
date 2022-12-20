package com.starters.hsge.data.api

import retrofit2.Call
import retrofit2.http.DELETE

interface FcmDeleteApi {
    @DELETE("api/auth/fcm/token")
    fun deleteFcmToken() : Call<Void>
}