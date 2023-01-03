package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.request.AddDogRequest
import com.starters.hsge.data.model.remote.request.EditDogRequest
import com.starters.hsge.data.model.remote.response.UserDogResponse
import java.io.File

interface UserDogRepository {

    suspend fun getMyDog(): List<UserDogResponse>

    suspend fun putDogData(petId: Int, img: File?, data: EditDogRequest)

    suspend fun postDogData(img: File, data: AddDogRequest)

    suspend fun deleteDogProfile(petId: Int)
}