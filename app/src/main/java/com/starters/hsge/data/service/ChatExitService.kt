package com.starters.hsge.data.service

import com.starters.hsge.data.api.ChatApi
import com.starters.hsge.data.interfaces.ChatExitInterface
import com.starters.hsge.data.model.remote.request.ChatExitRequest
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatExitService(val chatExitInterface: ChatExitInterface) {

    fun tryPostChatExit(roomId: Long, chatExitRequest: ChatExitRequest){
        val chatExitApi = RetrofitClient.sRetrofit.create(ChatApi::class.java)

        chatExitApi.postChatExit(roomId, chatExitRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                chatExitInterface.onPostChatExitSuccess(response.isSuccessful, response.code())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                chatExitInterface.onPostChatExitFailure(t.message ?: "통신 오류")
            }
        })
    }
}