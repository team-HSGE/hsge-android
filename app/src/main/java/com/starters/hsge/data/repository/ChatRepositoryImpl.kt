package com.starters.hsge.data.repository

import com.starters.hsge.data.api.ChatApi
import com.starters.hsge.data.model.remote.response.MessageInfoResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.mapper.mapToChatListInfo
import com.starters.hsge.domain.model.ChatListInfo
import com.starters.hsge.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: ChatApi
) : ChatRepository {

    override suspend fun getChatList(): Flow<List<ChatListInfo>?> = flow {
        emit(api.getChatList()?.map {
            it.mapToChatListInfo()
        })
    }

    override suspend fun getMessageList(roomId: Long): MessageInfoResponse {
        return api.getChatRecord(roomId)
    }

    override suspend fun postChatState(roomId: Long) {
        api.postChatState(roomId)
    }
}