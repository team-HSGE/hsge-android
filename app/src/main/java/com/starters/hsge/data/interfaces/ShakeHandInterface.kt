package com.starters.hsge.data.interfaces

import com.starters.hsge.data.model.remote.response.CurrentLocationPostResponse
import com.starters.hsge.data.model.remote.response.UsersLocationNearbyGetResponse

interface ShakeHandInterface {

    fun onPostCurrentLocationSuccess(currentLocationPostResponse: CurrentLocationPostResponse, isSuccess: Boolean)

    fun onPostCurrentLocationFailure(message: String)

    fun onGetShakeHandSuccess(usersLocationNearbyGetResponse: List<UsersLocationNearbyGetResponse>, isSuccess: Boolean)

    fun onGetShakeHandFailure(message: String)

    fun onDeleteUsersLocationSuccess(isSuccess: Boolean)

    fun onDeleteUsersLocationFailure(message: String)
}