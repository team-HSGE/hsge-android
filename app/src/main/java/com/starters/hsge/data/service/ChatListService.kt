package com.starters.hsge.data.service

import com.starters.hsge.data.api.ChatApi
import com.starters.hsge.data.interfaces.chatListInterface
import com.starters.hsge.data.model.remote.response.ChatListResponse
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatListService(val chatListInterface: chatListInterface) {
    fun tryGetChatList(){
        val chatListApi = RetrofitClient.sRetrofit.create(ChatApi::class.java)

        chatListApi.getChatList().enqueue(object : Callback<List<ChatListResponse?>?>{
            override fun onResponse(
                call: Call<List<ChatListResponse?>?>,
                response: Response<List<ChatListResponse?>?>
            ) {
                chatListInterface.onGetChatListSuccess(response.body(), response.isSuccessful, response.code())
            }

            override fun onFailure(call: Call<List<ChatListResponse?>?>, t: Throwable) {
                chatListInterface.onGetChatListFailure(t.message ?: "통신 오류")
            }
        })
    }
}