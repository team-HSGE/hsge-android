package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.UserRepository
import javax.inject.Inject

class GetIconUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() = userRepository.getIcons()
}