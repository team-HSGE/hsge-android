package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.ChatRepository
import javax.inject.Inject

class GetMessageListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(roomId: Long) = chatRepository.getMessageList(roomId)
}