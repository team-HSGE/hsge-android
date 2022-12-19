package com.starters.hsge.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface AddDogApi {
    @Multipart
    @POST("/api/pets")
    suspend fun postDogProfile(
        @Part dogPhoto: MultipartBody.Part,
        @PartMap dogProfile: HashMap<String, RequestBody>
    )
}