package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.DogProfileRepository
import com.starters.hsge.domain.repository.UserLocationRepository
import com.starters.hsge.domain.repository.UserProfileRepository
import javax.inject.Inject

class GetDogProfileUseCase @Inject constructor(
    private val dogProfileRepository: DogProfileRepository,
) {
    suspend fun invoke() = dogProfileRepository.getDogAge()
}