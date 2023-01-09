package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.response.MessageInfoResponse
import com.starters.hsge.domain.model.ChatListInfo
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getChatList(): Flow<List<ChatListInfo>?>

    suspend fun getMessageList(roomId: Long): MessageInfoResponse

    suspend fun postChatState(roomId: Long)
}