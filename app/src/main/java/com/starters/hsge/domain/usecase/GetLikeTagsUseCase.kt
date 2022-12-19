package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.DogOptionRepository
import javax.inject.Inject

class GetLikeTagsUseCase @Inject constructor(
    private val dogOptionRepository: DogOptionRepository
) {
    operator fun invoke() = dogOptionRepository.getDogLikeTags()
}