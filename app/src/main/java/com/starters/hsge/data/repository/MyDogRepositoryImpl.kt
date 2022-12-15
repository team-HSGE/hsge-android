package com.starters.hsge.data.repository

import com.starters.hsge.data.api.MyDogApi
import com.starters.hsge.data.model.remote.response.MyDogResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.MyDogRepository
import javax.inject.Inject

class MyDogRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val myDogApi: MyDogApi
) : MyDogRepository {

    override suspend fun getMyDog(): List<MyDogResponse> {
        return myDogApi.getMyDog()
    }
}