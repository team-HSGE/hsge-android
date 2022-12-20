package com.starters.hsge.data.interfaces

import com.starters.hsge.data.model.remote.response.UserInfoGetResponse

interface UserInfoGetInterface {

    fun onGetUserInfoSuccess(userInfoGetResponse: UserInfoGetResponse ,isSuccess: Boolean, code: Int)

    fun onGetUserInfoFailure(message: String)
}