package com.starters.hsge.domain.repository

import com.starters.hsge.common.constants.DogAge
import com.starters.hsge.common.constants.DogBreedLocal
import okhttp3.RequestBody
import java.io.File

interface DogProfileRepository {

    suspend fun getDogProfilePhoto(image: File, str: HashMap<String, RequestBody>)

    suspend fun getDogName()

    suspend fun getDogSex()

    suspend fun getDogNeuter()

    fun getDogAge(): List<DogAge>

    fun getDogBreed(): List<DogBreedLocal>

    suspend fun getDogLikeTag()

    suspend fun getDogDislikeTag()

    suspend fun postDogProfilePhoto()

    suspend fun postDogName()

    suspend fun postDogSex()

    suspend fun postDogNeuter()

    suspend fun postDogAge()

    suspend fun postDogBreed()

    suspend fun postDogLikeTag()

    suspend fun postDogDislikeTag()
}