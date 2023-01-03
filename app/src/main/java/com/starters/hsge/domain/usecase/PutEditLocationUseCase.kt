package com.starters.hsge.domain.usecase

import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.domain.repository.UserRepository
import javax.inject.Inject

class PutEditLocationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(location: UserLocationRequest) =
        userRepository.putLocation(location)
}