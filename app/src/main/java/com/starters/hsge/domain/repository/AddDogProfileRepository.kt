package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.request.AddDogRequest
import java.io.File

interface AddDogProfileRepository {
    suspend fun postDogData(img: File, data: AddDogRequest)
}