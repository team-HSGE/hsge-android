package com.starters.hsge.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.starters.hsge.domain.repository.RegisterPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException
import javax.inject.Inject

class RegisterPreferencesRepositoryImpl @Inject constructor(
    private val registerDataStore: DataStore<Preferences>,
) : RegisterPreferencesRepository {

    override suspend fun deleteAllData() {
        registerDataStore.edit {
            it.clear()
        }
    }

    override suspend fun setUserEmail(email: String) {
        registerDataStore.edit {
            it[EMAIL] = email
        }
    }

    override suspend fun setUserNickName(nickName: String) {
        registerDataStore.edit {
            it[NICK_NAME] = nickName
        }
    }

    override suspend fun setUserIcon(icon: Int) {
        registerDataStore.edit {
            it[ICON] = icon
        }
    }

    override suspend fun setDogName(name: String) {
        registerDataStore.edit {
            it[PET_NAME] = name
        }
    }

    override suspend fun setDogAge(age: String) {
        registerDataStore.edit {
            it[PET_AGE] = age
        }
    }

    override suspend fun setDogBreed(breed: String) {
        registerDataStore.edit {
            it[PET_BREED] = breed
        }
    }

    override suspend fun setDogSex(sex: String) {
        registerDataStore.edit {
            it[PET_SEX] = sex
        }
    }

    override suspend fun setIsNeuter(neuter: Boolean) {
        registerDataStore.edit {
            it[NEUTRALIZATION] = neuter
        }
    }

    override suspend fun setDogLikeTag(likeTag: String) {
        registerDataStore.edit {
            it[LIKE_TAG] = likeTag
        }
    }

    override suspend fun setDogDislikeTag(dislikeTag: String) {
        registerDataStore.edit {
            it[DISLIKE_TAG] = dislikeTag
        }
    }

    override suspend fun setUserLatitude(latitude: Double) {
        registerDataStore.edit {
            it[LATITUDE] = latitude
        }
    }

    override suspend fun setUserLongitude(longitude: Double) {
        registerDataStore.edit {
            it[LONGITUDE] = longitude
        }
    }

    override suspend fun getUserEmail(): String {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[EMAIL] ?: ""
                }.first()
        }
    }

    override suspend fun getUserNickName(): String {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[NICK_NAME] ?: ""
                }.first()
        }
    }

    override suspend fun getUserIcon(): Int {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[ICON]!!
                }.first()
        }
    }

    override suspend fun getDogName(): String {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[PET_NAME] ?: ""
                }.first()
        }
    }

    override suspend fun getDogAge(): String {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[PET_AGE] ?: ""
                }.first()
        }
    }

    override suspend fun getDogBreed(): String {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[PET_BREED] ?: ""
                }.first()
        }
    }

    override suspend fun getDogSex(): String {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[PET_SEX] ?: ""
                }.first()
        }
    }

    override suspend fun getIsNeuter(): Boolean {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[NEUTRALIZATION] ?: false
                }.first()
        }
    }

    override suspend fun getDogLikeTag(): String {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[LIKE_TAG] ?: ""
                }.first()
        }
    }

    override suspend fun getDogDislikeTag(): String {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[DISLIKE_TAG] ?: ""
                }.first()
        }
    }

    override suspend fun getUserLatitude(): Double {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[LATITUDE]!!
                }.first()
        }
    }

    override suspend fun getUserLongitude(): Double {
        return runBlocking(Dispatchers.IO) {
            registerDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    it[LONGITUDE]!!
                }.first()
        }
    }

    private companion object {
        val EMAIL = stringPreferencesKey("userEmail")
        val NICK_NAME = stringPreferencesKey("userNickName")
        val ICON = intPreferencesKey("userIcon")
        val PET_NAME = stringPreferencesKey("dogName")
        val PET_AGE = stringPreferencesKey("dogAge")
        val PET_BREED = stringPreferencesKey("dogBreed")
        val PET_SEX = stringPreferencesKey("dogSex")
        val NEUTRALIZATION = booleanPreferencesKey("isNeuter")
        val LIKE_TAG = stringPreferencesKey("dogLikeTag")
        val DISLIKE_TAG = stringPreferencesKey("dogDislikeTag")
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
    }
}