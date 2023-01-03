package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.UserDogRepository
import javax.inject.Inject

class DeleteDogUseCase @Inject constructor(
    private val userDogRepository: UserDogRepository
) {
    suspend operator fun invoke(petId: Int) = userDogRepository.deleteDogProfile(petId)
}