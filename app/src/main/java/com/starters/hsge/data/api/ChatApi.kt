package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.ChatExitRequest
import com.starters.hsge.data.model.remote.request.ReportRequest
import com.starters.hsge.data.model.remote.response.ChatListResponse
import com.starters.hsge.data.model.remote.response.MessageInfoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    // 채팅방 리스트 불러오기
    @GET("/api/chats")
    fun getChatList(): Call<List<ChatListResponse?>>

    // 메세지 리스트 불러오기
    @GET("/api/chats/{roomId}")
    suspend fun getChatRecord(
        @Path("roomId") roomId: Long
    ): MessageInfoResponse

    // 채팅방 활성화
    @POST("/api/chats/{roomId}/active")
    suspend fun postChatState(
        @Path("roomId") roomId: Long,
    ): Response<Void>

    // 채팅방 나가기
    @POST("/api/chats/{roomId}/leave")
    fun postChatExit(
        @Path("roomId") roomId: Long,
        @Body type: ChatExitRequest
    ): Call<Void>

    // 신고하기
    @POST("/api/users/report")
    fun postReport(
        @Body request: ReportRequest
    ): Call<Void>
}