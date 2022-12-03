package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.DogBreedResponse

interface DogOptionRepository {

    suspend fun getDogBreed(): DogBreedResponse

    suspend fun getDogAge()

    suspend fun getDogTag()
}