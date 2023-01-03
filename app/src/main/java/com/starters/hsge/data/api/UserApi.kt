package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.DistanceRequest
import com.starters.hsge.data.model.remote.request.UserInfoPutRequest
import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.data.model.remote.response.UserInfoGetResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {
    // 유저 정보 불러오기
    @GET("api/users")
    fun getUserInfo(): Call<UserInfoGetResponse>

    // 유저 정보 수정
    @PUT("api/users")
    fun putUserInfo(@Body changeUserInfo: UserInfoPutRequest): Call<Void>

    // 유저 위치 수정
    @PUT("/api/users/geolocation")
    suspend fun putLocationData(
        @Body location: UserLocationRequest
    )

    // 유저 반경 설정
    @PUT("/api/users/radius")
    fun putDistanceData(@Body request: DistanceRequest): Call<Void>
}