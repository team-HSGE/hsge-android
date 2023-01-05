package com.starters.hsge.data.service

import com.starters.hsge.data.api.LoginApi
import com.starters.hsge.data.interfaces.SettingsInterface
import com.starters.hsge.data.model.remote.request.FcmPostRequest
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsService(val settingsInterface: SettingsInterface) {

    fun tryDeleteFcmToken(token: FcmPostRequest){
        val fcmDeleteApi = RetrofitClient.sRetrofit.create(LoginApi::class.java)
        fcmDeleteApi.deleteFcmToken(token).enqueue(object :
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