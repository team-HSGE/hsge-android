package com.starters.hsge.data.api

import com.starters.hsge.presentation.main.chatroom.MessageInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatRecordApi {

    @GET("/api/chats/{roomId}")
    suspend fun getChatRecord(
        @Path("roomId") roomId: Long
    ): MessageInfo

    @POST("/api/chats/{roomId}/active")
    suspend fun postChatSate(
        @Path("roomId") roomId: Long,
    ): Response<Void>
}