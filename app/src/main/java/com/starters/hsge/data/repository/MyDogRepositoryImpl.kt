package com.starters.hsge.data.repository

import com.starters.hsge.data.api.UserDogApi
import com.starters.hsge.data.model.remote.response.MyDogResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.MyDogRepository
import javax.inject.Inject

class MyDogRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val userDogApi: UserDogApi
) : MyDogRepository {

    override suspend fun getMyDog(): List<MyDogResponse> {
        return userDogApi.getMyDog()
    }
}