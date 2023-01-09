package com.starters.hsge.presentation.main.chat

import androidx.lifecycle.viewModelScope
import com.starters.hsge.domain.usecase.GetChatListUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatListUseCase: GetChatListUseCase
) : BaseViewModel() {

    private val _chatListInfo: MutableStateFlow<ChatState> =
        MutableStateFlow(ChatState.Initial)
    val chatListInfo: StateFlow<ChatState>
        get() = _chatListInfo

    fun getChatList() {
        viewModelScope.launch {
            _chatListInfo.value = ChatState.Loading
            getChatListUseCase.invoke().catch { e ->
                Timber.d("!!에러 $e")
                _chatListInfo.value = ChatState.Failure(e)
            }.collect {
                Timber.d("!!뷰모델 $it")
                it?.let {
                    _chatListInfo.value = ChatState.Success(it)
                }
            }
        }
    }
}