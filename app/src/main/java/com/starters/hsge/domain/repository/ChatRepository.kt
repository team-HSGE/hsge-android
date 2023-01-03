package com.starters.hsge.domain.repository

import com.starters.hsge.presentation.main.chatroom.MessageInfo

interface ChatRepository {

    suspend fun getChatList(roomId: Long): MessageInfo

    suspend fun postChatState(roomId: Long)
}