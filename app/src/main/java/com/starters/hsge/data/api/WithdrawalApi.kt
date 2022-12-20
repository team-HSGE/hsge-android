package com.starters.hsge.data.api

import retrofit2.Call
import retrofit2.http.DELETE

interface WithdrawalApi {

    @DELETE("api/users/withdrawal")
    fun deleteUserInfo() : Call<Void>
}