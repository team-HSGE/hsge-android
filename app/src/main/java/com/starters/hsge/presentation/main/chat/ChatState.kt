package com.starters.hsge.presentation.main.chat

import com.starters.hsge.domain.model.ChatListInfo

sealed class ChatState {
    object Initial : ChatState()
    object Loading : ChatState()
    class Failure(val msg: Throwable) : ChatState()
    class Success(val data: List<ChatListInfo?>) : ChatState()
}