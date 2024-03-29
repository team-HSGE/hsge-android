package com.starters.hsge.data.service

import com.starters.hsge.data.api.LoginApi
import com.starters.hsge.data.interfaces.WithdrawalInterface
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WithdrawalService(val withdrawalInterface: WithdrawalInterface) {

    fun tryDeleteUserInfo() {
        val withdrawalApi = RetrofitClient.sRetrofit.create(LoginApi::class.java)
        withdrawalApi.deleteUserInfo().enqueue(object :
        Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                withdrawalInterface.onDeleteUserSuccess(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                withdrawalInterface.onDeleteUserFailure(t.message ?: "통신 오류")
            }
        })
    }
}