package com.starters.hsge.domain.repository

interface DogProfileRepository {

    suspend fun getDogProfilePhoto()

    suspend fun getDogName()

    suspend fun getDogSex()

    suspend fun getDogNeuter()

    suspend fun getDogAge()

    suspend fun getDogBreed()

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