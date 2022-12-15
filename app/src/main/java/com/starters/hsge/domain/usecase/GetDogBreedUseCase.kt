package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.mapper.mapToBreedMap
import com.starters.hsge.domain.repository.DogOptionRepository
import javax.inject.Inject

class GetDogBreedUseCase @Inject constructor(
    private val dogOptionRepository: DogOptionRepository
) {
    suspend operator fun invoke() = dogOptionRepository.getDogBreed().mapToBreedMap()
}