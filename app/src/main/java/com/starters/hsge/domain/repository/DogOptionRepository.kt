package com.starters.hsge.domain.repository

import com.starters.hsge.common.constants.DislikeTag
import com.starters.hsge.common.constants.LikeTag
import com.starters.hsge.data.model.remote.response.DogAgeResponse
import com.starters.hsge.data.model.remote.response.DogBreedResponse

interface DogOptionRepository {

    suspend fun getDogBreed(): DogBreedResponse

    suspend fun getDogAge(): DogAgeResponse

    fun getDogLikeTags(): List<LikeTag>

    fun getDogDislikeTags(): List<DislikeTag>
}