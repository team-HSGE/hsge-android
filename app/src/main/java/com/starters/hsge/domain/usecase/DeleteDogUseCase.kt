package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.DeleteDogRepository
import javax.inject.Inject

class DeleteDogUseCase @Inject constructor(
    private val deleteDogRepository: DeleteDogRepository
) {
    suspend operator fun invoke(petId: Int) = deleteDogRepository.deleteDogProfile(petId)
}