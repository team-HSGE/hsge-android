package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.response.MessageInfoResponse

interface ChatRepository {

    suspend fun getMessageList(roomId: Long): MessageInfoResponse

    suspend fun postChatState(roomId: Long)
}