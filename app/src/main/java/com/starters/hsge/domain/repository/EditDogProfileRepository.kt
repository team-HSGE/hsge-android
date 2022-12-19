package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.request.EditDogProfileRequest
import java.io.File

interface EditDogProfileRepository {
    suspend fun postDogData(petId: Int, img: File?, data: EditDogProfileRequest)
}