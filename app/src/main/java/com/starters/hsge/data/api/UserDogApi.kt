package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.response.UserDogResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UserDogApi {
    // 유저 반려견 리스트 불러오기
    @GET("/api/pets")
    suspend fun getMyDog(): List<UserDogResponse>

    // 유저 반려견 정보 수정
    @Multipart
    @PUT("api/pets/{petId}")
    suspend fun postEditDog(
        @Path("petId") petId: Int,
        @Part dogPhoto: MultipartBody.Part?,
        @PartMap editData: HashMap<String, RequestBody>
    )

    // 유저 반려견 추가
    @Multipart
    @POST("/api/pets")
    suspend fun postDogProfile(
        @Part dogPhoto: MultipartBody.Part,
        @PartMap dogProfile: HashMap<String, RequestBody>
    )

    // 유저 반려견 삭제
    @DELETE("/api/pets/{petId}")
    suspend fun deleteDog(
        @Path("petId") petId: Int
    )
}