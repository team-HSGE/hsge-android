package com.starters.hsge.presentation.main.chatroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.response.Message
import com.starters.hsge.data.model.remote.response.MessageInfoResponse
import com.starters.hsge.domain.usecase.GetMessageListUseCase
import com.starters.hsge.domain.usecase.PostChatRoomStateUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val getMessageListUseCase: GetMessageListUseCase,
    private val postChatRoomStateUseCase: PostChatRoomStateUseCase
) : BaseViewModel() {

    private val _chatList = MutableLiveData<MessageInfoResponse?>()
    var chatList: LiveData<MessageInfoResponse?> = _chatList

    var roomId: Long = 0
    var active: Boolean = false
    var partnerId: Long = 0
    var partnerNickName = ""
    var myId: Long = 0
    var messages = mutableListOf<Message>()


    fun getMessageInfo(roomId: Long): LiveData<MessageInfoResponse?> {
        viewModelScope.launch {
            _chatList.value = getMessageListUseCase(roomId)
        }
        return chatList
    }

    fun postChatRoomState(roomId: Long) {
        viewModelScope.launch {
            postChatRoomStateUseCase(roomId)
        }
    }
}