package com.starters.hsge.presentation.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.domain.repository.RegisterPreferencesRepository
import com.starters.hsge.domain.usecase.GetDogAgeUseCase
import com.starters.hsge.domain.usecase.GetDogBreedUseCase
import com.starters.hsge.domain.usecase.PostRegisterUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val postRegisterUseCase: PostRegisterUseCase,
    private val getDogBreedUseCase: GetDogBreedUseCase,
    private val getDogAgeUseCase: GetDogAgeUseCase,
    private val registerPreferencesRepository: RegisterPreferencesRepository
) : BaseViewModel() {

    lateinit var dogPhoto: File
    var dogAge = ""
    var dogBreed = ""

    private val _breedMap = MutableLiveData<Map<String, String>?>()
    val breedMap: LiveData<Map<String, String>?> = _breedMap

    private val _ageMap = MutableLiveData<Map<String,String>?>()
    val ageMap: LiveData<Map<String,String>?> = _ageMap

    init {
        getDogBreed()
        getDogAge()
    }

    suspend fun postRegisterInfo(img: File, data: RegisterInfo) = postRegisterUseCase(img, data)

    private fun getDogBreed() {
        viewModelScope.launch {
            _breedMap.value = getDogBreedUseCase()
        }
    }

    private fun getDogAge() {
        viewModelScope.launch {
            _ageMap.value = getDogAgeUseCase()
        }
    }

    suspend fun deleteAllInfo() {
        registerPreferencesRepository.deleteAllData()
    }

    suspend fun saveDogName(name: String) {
        registerPreferencesRepository.setDogName(name)
    }

    suspend fun saveDogSex(sex: String) {
        registerPreferencesRepository.setDogSex(sex)
    }

    suspend fun saveDogNeuter(neuter: Boolean) {
        registerPreferencesRepository.setIsNeuter(neuter)
    }

    suspend fun saveDogAge(age: String) {
        registerPreferencesRepository.setDogAge(age)
    }

    suspend fun saveDogBreed(breed: String) {
        registerPreferencesRepository.setDogBreed(breed)
    }

    suspend fun saveDogLikeTag(likeTag: String) {
        registerPreferencesRepository.setDogLikeTag(likeTag)
    }

    suspend fun saveDogDislikeTag(disLikeTag: String) {
        registerPreferencesRepository.setDogDislikeTag(disLikeTag)
    }

    suspend fun saveUserLatitude(latitude: Double) {
        registerPreferencesRepository.setUserLatitude(latitude)
    }

    suspend fun saveUserLongitude(longitude: Double) {
        registerPreferencesRepository.setUserLongitude(longitude)
    }

    suspend fun fetchDogName(): String {
        return registerPreferencesRepository.getDogName()
    }

    suspend fun fetchDogSex(): String {
        return registerPreferencesRepository.getDogSex()
    }

    suspend fun fetchDogNeuter(): Boolean {
        return registerPreferencesRepository.getIsNeuter()
    }

    suspend fun fetchDogAge(): String {
        return registerPreferencesRepository.getDogAge()
    }

    suspend fun fetchDogBreed(): String {
        return registerPreferencesRepository.getDogBreed()
    }

    suspend fun fetchDogLikeTag(): String {
        return registerPreferencesRepository.getDogLikeTag()
    }

    suspend fun fetchDogDisLikeTag(): String {
        return registerPreferencesRepository.getDogDislikeTag()
    }

    suspend fun fetchUserLatitude(): Double {
        return registerPreferencesRepository.getUserLatitude()
    }

    suspend fun fetchUserLongitude(): Double {
        return registerPreferencesRepository.getUserLongitude()
    }
}