package com.starters.hsge.data.interfaces

interface SettingsInterface {

    fun onDeleteFcmTokenSuccess(isSuccess: Boolean)

    fun onDeleteFcmTokenFailure(message: String)
}