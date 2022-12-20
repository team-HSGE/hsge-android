package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.response.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface UserApi {
    @Multipart
    @POST("api/auth/signup")
    suspend fun postSignUp(
        @Part dogPhoto: MultipartBody.Part,
        @PartMap registerData: HashMap<String, RequestBody>
    ): SignUpResponse
}