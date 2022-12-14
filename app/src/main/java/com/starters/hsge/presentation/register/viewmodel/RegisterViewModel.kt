package com.starters.hsge.presentation.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starters.hsge.ApiResult
import com.starters.hsge.App.Companion.prefs
import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.domain.repository.DogProfileRepository
import com.starters.hsge.domain.repository.RegisterPreferencesRepository
import com.starters.hsge.domain.usecase.GetDogAgeUseCase
import com.starters.hsge.domain.usecase.GetDogBreedUseCase
import com.starters.hsge.domain.usecase.PostRegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val postRegisterUseCase: PostRegisterUseCase,
    private val getDogBreedUseCase: GetDogBreedUseCase,
    private val getDogAgeUseCase: GetDogAgeUseCase,
    private val registerPreferencesRepository: RegisterPreferencesRepository,
    private val dogProfileRepository: DogProfileRepository
) : ViewModel() {

    var img = ""

    private val _breedMap = MutableLiveData<Map<String, String>?>()
    val breedMap: LiveData<Map<String, String>?> = _breedMap

    private val _ageMap = MutableLiveData<Map<String,String>?>()
    val ageMap: LiveData<Map<String,String>?> = _ageMap

    init {
        getDogBreed()
        getDogAge()
    }

    fun postRegisterInfo(img: File, data: RegisterInfo){
        viewModelScope.launch {
            postRegisterUseCase(img, data).collectLatest { result ->
                when (result) {
                    is ApiResult.Success -> {
                        prefs.edit().putString("BearerAccessToken", "Bearer ${result.value.accessToken}").apply()
                        prefs.edit().putString("BearerRefreshToken", "Bearer ${result.value.refreshToken}").apply()
                        prefs.edit().putString("NormalAccessToken", result.value.accessToken).apply()
                        prefs.edit().putString("NormalRefreshToken", result.value.refreshToken).apply()
                    }
                    is ApiResult.Empty -> {

                    }
                    is ApiResult.Error -> {
                        // 이동

                    }

                }
            }
        }
    }

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

    //테스트를 위한 Age 로컬데이터
    fun testDogAgeToLocal() = dogProfileRepository.getDogAge()

    // datastore에서 값 가져오기
    fun fetchUserEmail() = registerPreferencesRepository.userEmail

    fun fetchUserNickname() = registerPreferencesRepository.userNickName

    fun fetchUserIcon() = registerPreferencesRepository.userIcon

    fun fetchDogName() = registerPreferencesRepository.dogName

    fun fetchDogPhoto() = registerPreferencesRepository.dogPhoto

    fun fetchDogSex() = registerPreferencesRepository.dogSex

    fun fetchDogNeuter() = registerPreferencesRepository.dogNeuter

    fun fetchDogAgeForView() = registerPreferencesRepository.dogAgeForView

    fun fetchDogAge() = registerPreferencesRepository.dogAge

    fun fetchDogBreedForView() = registerPreferencesRepository.dogBreedForView

    fun fetchDogBreed() = registerPreferencesRepository.dogBreed

    fun fetchDogLikeTag() = registerPreferencesRepository.dogLikeTag

    fun fetchDogDislikeTag() = registerPreferencesRepository.dogDislikeTag

    fun fetchUserLatitude() = registerPreferencesRepository.userLatitude

    fun fetchUserLongitude() = registerPreferencesRepository.userLongitude


    // datastore에 값 저장하기
    fun saveUserEmail(email: String) {
        viewModelScope.launch {
            registerPreferencesRepository.setUserEmail(email)
        }
    }

    fun saveUserNickname(nickname: String) {
        viewModelScope.launch {
            registerPreferencesRepository.setUserNickName(nickname)
        }
    }

    fun saveUserIcon(icon: Int) {
        viewModelScope.launch {
            registerPreferencesRepository.setUserIcon(icon)
        }
    }

    fun saveDogName(name: String) {
        viewModelScope.launch {
            registerPreferencesRepository.setDogName(name)
        }
    }

    fun saveDogPhoto(uriStr: String) {
        viewModelScope.launch {
            registerPreferencesRepository.setDogPhoto(uriStr)
        }
    }

    suspend fun saveDogSex(sex: String) {
        registerPreferencesRepository.setDogSex(sex)
    }

    suspend fun saveDogNeuter(neuter: Boolean) {
        registerPreferencesRepository.setIsNeuter(neuter)
    }

    suspend fun saveDogAgeForView(age: String) {
        registerPreferencesRepository.setDogAgeForView(age)
    }

    suspend fun saveDogAge(age: String) {
        registerPreferencesRepository.setDogAge(age)
    }

    suspend fun saveDogBreedForView(breed: String) {
        registerPreferencesRepository.setDogBreedForView(breed)
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
}