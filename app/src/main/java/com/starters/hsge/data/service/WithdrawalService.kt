package com.starters.hsge.data.service

import com.starters.hsge.data.api.FcmDeleteApi
import com.starters.hsge.data.api.WithdrawalApi
import com.starters.hsge.data.interfaces.WithdrawalInterface
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WithdrawalService(val withdrawalInterface: WithdrawalInterface) {

    fun tryDeleteUserInfo() {
        val withdrawalApi = RetrofitClient.sRetrofit.create(WithdrawalApi::class.java)
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

    fun tryDeleteFcmToken(){
        val fcmDeleteApi = RetrofitClient.sRetrofit.create(FcmDeleteApi::class.java)
        fcmDeleteApi.deleteFcmToken().enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                withdrawalInterface.onDeleteFcmTokenSuccess(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                withdrawalInterface.onDeleteFcmTokenFailure(t.message ?: "통신 오류")
            }
        })
    }

}