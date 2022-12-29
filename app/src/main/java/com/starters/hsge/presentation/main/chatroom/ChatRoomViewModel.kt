package com.starters.hsge.presentation.main.chatroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.domain.repository.ChatListRepository
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatListRepository: ChatListRepository
) : BaseViewModel() {

    private val _chatList = MutableLiveData<MessageInfo>()
    var chatList: LiveData<MessageInfo> = _chatList

    var addedMessage = MutableLiveData<Message>()
    var messageList = MediatorLiveData<MutableList<Message>>()

    var roomId: Long = 0
    var active: Boolean = false
    var partnerId: Long = 0
    var partnerNickName = ""
    var myId: Long = 0

    //private val messageList = MediatorLiveData<MutableList<Message>>()


    fun getChatInfo(roomId: Long): LiveData<MessageInfo> {
        viewModelScope.launch {
            _chatList.value = chatListRepository.getChatList(roomId)
        }
        return chatList
    }
}