package com.starters.hsge.data.repository

import com.starters.hsge.data.api.DogOptionApi
import com.starters.hsge.data.model.remote.DogBreedResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.DogOptionRepository
import javax.inject.Inject

class DogOptionRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val dogOptionApi: DogOptionApi
) : DogOptionRepository {

    override suspend fun getDogBreed(): DogBreedResponse {
        return dogOptionApi.getBreed()
    }

    override suspend fun getDogAge() {
        TODO("Not yet implemented")
    }

    override suspend fun getDogTag() {
        TODO("Not yet implemented")
    }

}