package com.starters.hsge.presentation.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.response.Data
import com.starters.hsge.domain.repository.DogProfileRepository
import com.starters.hsge.domain.usecase.GetDogAgeUseCase
import com.starters.hsge.domain.usecase.GetDogBreedUseCase
import com.starters.hsge.domain.usecase.GetDogBreedUseCase2
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dogProfileRepository: DogProfileRepository,
    private val getDogBreedUseCase2: GetDogBreedUseCase2,
    private val getDogAgeUseCase: GetDogAgeUseCase,
    private val getDogBreedUseCase: GetDogBreedUseCase,
) : BaseViewModel() {

    var dogName = ""
    var dogPhoto = ""
    var dogSex = ""
    var dogNeuter = false
    var dogAge = ""
    var dogBreed = ""
    var dogLikTag = ""
    var dogDisLikeTag = ""

    private val _breedList = MutableLiveData<List<Data>>()
    val breedList: LiveData<List<Data>> = _breedList

    suspend fun loadImage(image: File, str: HashMap<String, RequestBody>) {

        // usecase에서 데이터 변환
        // usecase에 집어넣기
        dogProfileRepository.getDogProfilePhoto(image, str)
    }

    suspend fun postFirstProfile() {

    }

    fun getDogBreed2() = getDogBreedUseCase2()

    suspend fun getDogBreed(): LiveData<List<Data>> {
        viewModelScope.launch {
            _breedList.value = getDogBreedUseCase().data
        }
        return breedList
    }

    fun getDogAge() = getDogAgeUseCase()
}