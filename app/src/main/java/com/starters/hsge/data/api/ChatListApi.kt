package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.response.ChatListResponse
import retrofit2.Call
import retrofit2.http.GET

interface ChatListApi {
    @GET("/api/chats")
    fun getChatList(): Call<List<ChatListResponse?>>
}