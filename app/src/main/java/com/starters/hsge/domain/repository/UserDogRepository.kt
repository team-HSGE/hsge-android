package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.request.AddDogRequest
import com.starters.hsge.data.model.remote.request.EditDogRequest
import com.starters.hsge.data.model.remote.response.UserDogResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.io.File

interface UserDogRepository {

    suspend fun getMyDog(): Flow<List<UserDogResponse>>

    suspend fun putDogData(petId: Int, img: File?, data: EditDogRequest): Response<Void>

    suspend fun postDogData(img: File, data: AddDogRequest): Response<Void>

    suspend fun deleteDogProfile(petId: Int): Response<Void>
}