package com.starters.hsge.domain.repository

import com.starters.hsge.common.constants.DogAge
import com.starters.hsge.common.constants.DogBreedLocal
import okhttp3.RequestBody
import java.io.File

interface DogProfileRepository {

    suspend fun getDogProfilePhoto(image: File, str: HashMap<String, RequestBody>)

    fun getDogAge(): List<DogAge>

    fun getDogBreed2(): List<DogBreedLocal>

}