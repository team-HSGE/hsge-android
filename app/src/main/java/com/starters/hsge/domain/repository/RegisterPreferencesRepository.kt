package com.starters.hsge.domain.repository

import kotlinx.coroutines.flow.Flow

interface RegisterPreferencesRepository {

    val userEmail: Flow<String>

    val userNickName: Flow<String>

    val userIcon: Flow<Int>

    val dogName: Flow<String>

    val dogPhoto: Flow<String>

    val dogSex: Flow<String>

    val dogNeuter: Flow<Boolean>

    val dogAgeForView: Flow<String>

    val dogAge: Flow<String>

    val dogBreedForView: Flow<String>

    val dogBreed: Flow<String>

    val dogLikeTag: Flow<String>

    val dogDislikeTag: Flow<String>

    val userLatitude: Flow<Double>

    val userLongitude: Flow<Double>

    val userLocation: Flow<String>

    val currentRoomId: Flow<Long>

    val isChatRoom: Flow<Boolean>

    suspend fun setUserEmail(email: String)

    suspend fun setUserNickName(nickName: String)

    suspend fun setUserIcon(icon: Int)

    suspend fun setDogName(name: String)

    suspend fun setDogPhoto(uriStr: String)

    suspend fun setDogAgeForView(age: String)

    suspend fun setDogAge(age: String)

    suspend fun setDogBreedForView(breed: String)

    suspend fun setDogBreed(breed: String)

    suspend fun setDogSex(sex: String)

    suspend fun setIsNeuter(neuter: Boolean)

    suspend fun setDogLikeTag(likeTag: String)

    suspend fun setDogDislikeTag(dislikeTag: String)

    suspend fun setUserLatitude(latitude: Double)

    suspend fun setUserLongitude(longitude: Double)

    suspend fun setUserLocation(location: String)

    suspend fun setCurrentRoomId(roomId: Long)

    suspend fun setIsChatRoom(chatRoom: Boolean)

    suspend fun deleteUserLocation()

    suspend fun deleteAllData()

}