package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.response.MyDogResponse

interface MyDogRepository {

    suspend fun getMyDog(): List<MyDogResponse>
}