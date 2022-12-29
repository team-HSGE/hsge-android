package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.ChatListRepository
import javax.inject.Inject

class PostChatRoomStateUseCase @Inject constructor(
    private val chatListRepository: ChatListRepository
) {
    suspend operator fun invoke(roomId: Long) = chatListRepository.postChatState(roomId)
}