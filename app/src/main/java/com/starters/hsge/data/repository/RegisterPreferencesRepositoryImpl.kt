package com.starters.hsge.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.starters.hsge.domain.repository.RegisterPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RegisterPreferencesRepositoryImpl @Inject constructor(
    private val registerDataStore: DataStore<Preferences>
) : RegisterPreferencesRepository {

    override val userEmail: Flow<String>
        get() = registerDataStore.data.map {
            it[EMAIL] ?: ""
        }

    override val userNickName: Flow<String>
        get() = registerDataStore.data.map {
            it[NICK_NAME] ?: ""
        }
    override val userIcon: Flow<Int>
        get() = registerDataStore.data.map {
            it[ICON] ?: 0
        }

    override val dogName: Flow<String>
        get() = registerDataStore.data.map {
            it[PET_NAME] ?: ""
        }
    override val dogPhoto: Flow<String>
        get() = registerDataStore.data.map {
            it[PET_PHOTO] ?: ""
        }

    override val dogSex: Flow<String>
        get() = registerDataStore.data.map {
            it[PET_SEX] ?: ""
        }

    override val dogNeuter: Flow<Boolean>
        get() = registerDataStore.data.map {
            it[NEUTRALIZATION] ?: false
        }

    override val dogAgeForView: Flow<String>
        = registerDataStore.data.map {
            it[PET_AGE_FOR_VIEW] ?: ""
    }

    override val dogAge: Flow<String>
        get() = registerDataStore.data.map {
            it[PET_AGE] ?: ""
        }

    override val dogBreedForView: Flow<String>
        get() = registerDataStore.data.map {
            it[PET_BREED_FOR_VIEW] ?: ""
        }

    override val dogBreed: Flow<String>
        get() = registerDataStore.data.map {
            it[PET_BREED] ?: ""
        }

    override val dogLikeTag: Flow<String>
        get() = registerDataStore.data.map {
            it[LIKE_TAG] ?: ""
        }

    override val dogDislikeTag: Flow<String>
        get() = registerDataStore.data.map {
            it[DISLIKE_TAG] ?: ""
        }

    override val userLatitude: Flow<Double>
        get() = registerDataStore.data.map {
            it[LATITUDE] ?: 0.0
        }

    override val userLongitude: Flow<Double>
        get() = registerDataStore.data.map {
            it[LONGITUDE] ?: 0.0
        }

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

    override suspend fun setDogPhoto(uriStr: String) {
        registerDataStore.edit {
            it[PET_PHOTO] = uriStr
        }
    }

    override suspend fun setDogAgeForView(age: String) {
        registerDataStore.edit {
            it[PET_AGE_FOR_VIEW] = age
        }
    }

    override suspend fun setDogAge(age: String) {
        registerDataStore.edit {
            it[PET_AGE] = age
        }
    }

    override suspend fun setDogBreedForView(breed: String) {
        registerDataStore.edit {
            it[PET_BREED_FOR_VIEW] = breed
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

//    override suspend fun getUserEmail(): String {
//        return runBlocking(Dispatchers.IO) {
//            registerDataStore.data
//                .catch { exception ->
//                    if (exception is IOException) {
//                        emit(emptyPreferences())
//                    } else {
//                        throw exception
//                    }
//                }
//                .map {
//                    it[EMAIL] ?: ""
//                }.first()
//        }
//    }

    private companion object {
        val EMAIL = stringPreferencesKey("userEmail")
        val NICK_NAME = stringPreferencesKey("userNickName")
        val ICON = intPreferencesKey("userIcon")
        val PET_NAME = stringPreferencesKey("dogName")
        val PET_PHOTO = stringPreferencesKey("dogPhoto")
        val PET_AGE_FOR_VIEW = stringPreferencesKey("dogAgeForView")
        val PET_AGE = stringPreferencesKey("dogAge")
        val PET_BREED_FOR_VIEW = stringPreferencesKey("dogBreedForView")
        val PET_BREED = stringPreferencesKey("dogBreed")
        val PET_SEX = stringPreferencesKey("dogSex")
        val NEUTRALIZATION = booleanPreferencesKey("isNeuter")
        val LIKE_TAG = stringPreferencesKey("dogLikeTag")
        val DISLIKE_TAG = stringPreferencesKey("dogDislikeTag")
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
    }
}