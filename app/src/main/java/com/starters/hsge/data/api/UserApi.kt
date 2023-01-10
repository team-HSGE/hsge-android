package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.DistanceRequest
import com.starters.hsge.data.model.remote.request.EditUserRequest
import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.data.model.remote.response.UserInfoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {
    // 유저 정보 불러오기
    @GET("api/users")
    suspend fun getUserInfo(): UserInfoResponse

    // 유저 정보 수정
    @PUT("api/users")
    suspend fun putUserInfo(
        @Body userInfo: EditUserRequest
    ): Response<Void>

    // 유저 위치 수정
    @PUT("/api/users/geolocation")
    suspend fun putLocationData(
        @Body location: UserLocationRequest
    ): Response<Void>

    // 유저 반경 설정
    @PUT("/api/users/radius")
    fun putDistanceData(
        @Body request: DistanceRequest
    ): Call<Void>
}