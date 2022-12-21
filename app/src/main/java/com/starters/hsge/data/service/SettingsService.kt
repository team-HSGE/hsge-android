package com.starters.hsge.data.service

import com.starters.hsge.data.api.FcmDeleteApi
import com.starters.hsge.data.interfaces.SettingsInterface
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsService(val settingsInterface: SettingsInterface) {

    fun tryDeleteFcmToken(){
        val fcmDeleteApi = RetrofitClient.sRetrofit.create(FcmDeleteApi::class.java)
        fcmDeleteApi.deleteFcmToken().enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                settingsInterface.onDeleteFcmTokenSuccess(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                settingsInterface.onDeleteFcmTokenFailure(t.message ?: "통신 오류")
            }
        })
    }
}