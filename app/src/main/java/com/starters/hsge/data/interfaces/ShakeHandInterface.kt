package com.starters.hsge.data.interfaces

import com.starters.hsge.data.model.remote.response.ShakeHandResponse

interface ShakeHandInterface {

    fun onPostCurrentLocationSuccess(isSuccess: Boolean)

    fun onPostCurrentLocationFailure(message: String)

    fun onGetShakeHandSuccess(shakeHandResponse: List<ShakeHandResponse>, isSuccess: Boolean)

    fun onGetShakeHandFailure(message: String)
}