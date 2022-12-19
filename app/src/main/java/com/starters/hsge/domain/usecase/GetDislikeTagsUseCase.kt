package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.DogOptionRepository
import javax.inject.Inject

class GetDislikeTagsUseCase @Inject constructor(
    private val dogOptionRepository: DogOptionRepository
) {
    operator fun invoke() = dogOptionRepository.getDogDislikeTags()
}