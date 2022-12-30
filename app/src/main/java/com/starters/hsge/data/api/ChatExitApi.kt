package com.starters.hsge.data.api

import com.starters.hsge.data.model.remote.request.ChatExitRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatExitApi {
    @POST("/api/chats/{roomId}/leave")
    fun postChatExit (@Path("roomId") roomId: Long,
                      @Body type: ChatExitRequest
    ) : Call<Void>
}