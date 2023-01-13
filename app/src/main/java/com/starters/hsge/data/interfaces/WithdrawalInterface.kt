package com.starters.hsge.data.interfaces

interface WithdrawalInterface {

    fun onDeleteUserSuccess(isSuccess: Boolean)

    fun onDeleteUserFailure(message: String)
}