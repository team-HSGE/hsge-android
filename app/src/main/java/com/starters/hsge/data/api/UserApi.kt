package com.starters.hsge.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApi {
    @Multipart
    @POST("")
    suspend fun postSignUp(
        @Part dogPhoto: MultipartBody.Part,
        @Part registerData: RequestBody
    )
}