package com.starters.hsge.domain.repository

interface RegisterPreferencesRepository {

    suspend fun deleteAllData()

    suspend fun setUserEmail(email: String)

    suspend fun setUserNickName(nickName: String)

    suspend fun setUserIcon(icon: Int)

    suspend fun setDogName(name: String)

    suspend fun setDogAge(age: String)

    suspend fun setDogBreed(breed: String)

    suspend fun setDogSex(sex: String)

    suspend fun setIsNeuter(neuter: Boolean)

    suspend fun setDogLikeTag(likeTag: String)

    suspend fun setDogDislikeTag(dislikeTag: String)

    suspend fun setUserLatitude(latitude: Double)

    suspend fun setUserLongitude(longitude: Double)

    suspend fun getUserEmail(): String

    suspend fun getUserNickName(): String

    suspend fun getUserIcon(): Int

    suspend fun getDogName(): String

    suspend fun getDogAge(): String

    suspend fun getDogBreed(): String

    suspend fun getDogSex(): String

    suspend fun getIsNeuter(): Boolean

    suspend fun getDogLikeTag(): String

    suspend fun getDogDislikeTag(): String

    suspend fun getUserLatitude(): Double

    suspend fun getUserLongitude(): Double
}