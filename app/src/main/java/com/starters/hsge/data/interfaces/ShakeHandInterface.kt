package com.starters.hsge.data.interfaces

interface ShakeHandInterface {

    fun onPostCurrentLocationSuccess(isSuccess: Boolean)

    fun onPostCurrentLocationFailure(message: String)
}