package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.CurrentLocationPostRequest
import com.starters.hsge.data.model.remote.request.UsersLocationDeleteRequest
import com.starters.hsge.data.model.remote.response.CurrentLocationPostResponse
import com.starters.hsge.data.model.remote.response.UsersLocationNearbyGetResponse
import retrofit2.Call
import retrofit2.http.*

interface ShakeHandApi {
    @POST("api/users/location")
    fun postUsersLocation(
        @Body currentLocation: CurrentLocationPostRequest
    ): Call<CurrentLocationPostResponse>

    @GET("api/users/location/nearby")
    fun getUsersLocationNearby(): Call<List<UsersLocationNearbyGetResponse>>

    @HTTP(method = "DELETE", path = "api/users/location/delete", hasBody = true)
    fun deleteUsersLocation(
        @Body deleteUsersLocation: UsersLocationDeleteRequest
    ) : Call<Void>

    @POST("api/waves/to/{userId}")
    fun postShakeHand(@Path ("userId") userId: Long
    ): Call<Void>
}