package com.starters.hsge.data.repository

import com.starters.hsge.data.api.ChatApi
import com.starters.hsge.data.model.remote.response.MessageInfoResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: ChatApi
) : ChatRepository {

    override suspend fun getMessageList(roomId: Long): MessageInfoResponse {
        return api.getChatRecord(roomId)
    }

    override suspend fun postChatState(roomId: Long) {
        api.postChatState(roomId)
    }
}