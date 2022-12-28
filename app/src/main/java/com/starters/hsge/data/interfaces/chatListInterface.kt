package com.starters.hsge.data.interfaces

import com.starters.hsge.data.model.remote.response.ChatListResponse

interface chatListInterface {

      fun onGetChatListSuccess(chatListResponse: List<ChatListResponse?>?, isSuccess: Boolean, code: Int)

      fun onGetChatListFailure(message: String)
}