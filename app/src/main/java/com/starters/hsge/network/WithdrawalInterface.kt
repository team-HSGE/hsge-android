package com.starters.hsge.network

import retrofit2.Call
import retrofit2.http.DELETE

interface WithdrawalInterface {

    @DELETE("api/users/withdrawal")
    fun deleteUserInfo() : Call<Void>
}