package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.CheckTokenRequest
import com.starters.hsge.data.model.remote.request.FcmPostRequest
import com.starters.hsge.data.model.remote.request.LoginRequest
import com.starters.hsge.data.model.remote.response.CheckTokenResponse
import com.starters.hsge.data.model.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface LoginApi {
    // 로그인
    @POST("api/auth/login")
    fun postLogin(
        @Body accessToken: LoginRequest
    ): Call<LoginResponse>

    // access token 체크
    @POST("api/auth/refresh-token")
    fun postToken(
        @Body checkToken: CheckTokenRequest
    ): Call<CheckTokenResponse?>

    // fcm token 전송
    @POST("api/auth/fcm/token")
    fun postFcmToken(
        @Body token: FcmPostRequest
    ): Call<Void>

    // fcm token 삭제
    @DELETE("api/auth/fcm/token")
    fun deleteFcmToken(): Call<Void>

    // 회원 탈퇴
    @DELETE("api/users/withdrawal")
    fun deleteUserInfo(): Call<Void>
}