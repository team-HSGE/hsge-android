package com.starters.hsge.data.interfaces

import com.starters.hsge.data.model.remote.response.CheckTokenResponse

interface CheckTokenInterface {

    fun onPostCheckTokenSuccess(checkTokenResponse: CheckTokenResponse, isSuccess: Boolean, code: Int)

    fun onPostCheckTokenFailure(message: String)
}