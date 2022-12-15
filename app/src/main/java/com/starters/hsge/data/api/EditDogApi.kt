package com.starters.hsge.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface EditDogApi {
    @Multipart
    @PUT("api/pets/{petId}")
    suspend fun postEditDog(
        @Path("petId") petId: Int,
        @Part dogPhoto: MultipartBody.Part?,
        @PartMap editData: HashMap<String, RequestBody>
    )
}