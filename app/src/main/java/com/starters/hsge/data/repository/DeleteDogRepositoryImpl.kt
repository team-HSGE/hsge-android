package com.starters.hsge.data.repository

import com.starters.hsge.data.api.UserDogApi
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.DeleteDogRepository
import javax.inject.Inject

class DeleteDogRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: UserDogApi
) : DeleteDogRepository{
    override suspend fun deleteDogProfile(petId: Int) {
        api.deleteDog(petId)
    }
}