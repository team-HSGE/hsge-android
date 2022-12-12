package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.response.DogAgeResponse
import com.starters.hsge.data.model.remote.response.DogBreedResponse

interface DogOptionRepository {

    suspend fun getDogBreed(): DogBreedResponse

    suspend fun getDogAge(): DogAgeResponse

    suspend fun getDogTag()
}