package com.starters.hsge.data.interfaces

import com.starters.hsge.data.model.remote.response.LoginResponse

interface LoginInterface {

    fun onPostAccessTokenSuccess(loginResponse: LoginResponse, isSuccess: Boolean, code: Int)

    fun onPostAccessTokenFailure(message: String)

    fun onPostFcmTokenSuccess(isSuccess: Boolean)

    fun onPostFcmTokenFailure(message: String)
}