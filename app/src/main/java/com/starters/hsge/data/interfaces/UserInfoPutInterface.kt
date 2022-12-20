package com.starters.hsge.data.interfaces

interface UserInfoPutInterface {

    fun onPutUserInfoSuccess(isSuccess: Boolean, code: Int)

    fun onPutUserInfoFailure(message: String)
}