package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.getUserInfo()
}