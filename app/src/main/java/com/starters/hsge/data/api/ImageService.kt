package com.starters.hsge.data.api

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageService {
    @Multipart
    @POST("/images")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    )
}