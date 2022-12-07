package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.Mapper.mapToAgeMap
import com.starters.hsge.domain.repository.DogOptionRepository
import javax.inject.Inject

class GetDogAgeUseCase @Inject constructor(
    private val dogOptionRepository: DogOptionRepository
) {
    suspend operator fun invoke() = dogOptionRepository.getDogAge().mapToAgeMap()
}