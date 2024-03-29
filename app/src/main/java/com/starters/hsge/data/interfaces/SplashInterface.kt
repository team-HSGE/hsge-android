package com.starters.hsge.data.interfaces

import com.starters.hsge.data.model.remote.response.CheckTokenResponse

interface SplashInterface {

    fun onPostCheckTokenSuccess(checkTokenResponse: CheckTokenResponse?, isSuccess: Boolean, code: Int, error: String?)

    fun onPostCheckTokenFailure(message: String)
}