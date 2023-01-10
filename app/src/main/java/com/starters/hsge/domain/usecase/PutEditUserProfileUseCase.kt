package com.starters.hsge.domain.usecase

import com.starters.hsge.data.model.remote.request.EditUserRequest
import com.starters.hsge.domain.repository.UserRepository
import javax.inject.Inject

class PutEditUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userInfo: EditUserRequest) = userRepository.putUserInfo(userInfo)
}