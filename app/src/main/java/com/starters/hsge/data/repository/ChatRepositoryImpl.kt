package com.starters.hsge.data.repository

import com.starters.hsge.data.api.ChatApi
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.ChatRepository
import com.starters.hsge.presentation.main.chatroom.MessageInfo
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: ChatApi
) : ChatRepository {

    override suspend fun getChatList(roomId: Long): MessageInfo {
        return api.getChatRecord(roomId)
    }

    override suspend fun postChatState(roomId: Long) {
        api.postChatState(roomId)
    }
}