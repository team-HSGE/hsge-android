package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.DELETE

interface FcmDeleteInterface {
    @DELETE("api/auth/fcm/token")
    fun deleteFcmToken() : Call<Void>
}