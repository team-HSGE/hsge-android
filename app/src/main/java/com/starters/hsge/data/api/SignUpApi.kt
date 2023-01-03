package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.NicknameRequest
import com.starters.hsge.data.model.remote.response.NicknameResponse
import com.starters.hsge.data.model.remote.response.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface SignUpApi {
    // 회원가입
    @Multipart
    @POST("api/auth/signup")
    suspend fun postSignUp(
        @Part dogPhoto: MultipartBody.Part,
        @PartMap registerData: HashMap<String, RequestBody>
    ): SignUpResponse

    // 닉네임 중복체크
    @POST("api/auth/duplicate-nickname")
    fun postNickname(
        @Body nickname: NicknameRequest
    ): Call<NicknameResponse>
}