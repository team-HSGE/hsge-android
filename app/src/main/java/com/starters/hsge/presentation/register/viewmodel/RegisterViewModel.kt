package com.starters.hsge.presentation.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.response.Breed
import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.domain.repository.DogProfileRepository
import com.starters.hsge.domain.usecase.GetDogAgeUseCase
import com.starters.hsge.domain.usecase.GetDogBreedUseCase
import com.starters.hsge.domain.usecase.GetDogBreedUseCase2
import com.starters.hsge.domain.usecase.PostRegisterUseCase
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
    private val postRegisterUseCase: PostRegisterUseCase
) : BaseViewModel() {

    var dogName = ""
    var dogPhoto = ""
    var dogSex = ""
    var dogNeuter = false
    var dogAge = ""
    var dogBreed = ""
    var dogLikTag = ""
    var dogDisLikeTag = ""

    private val _breedList = MutableLiveData<List<Breed>>()
    val breedList: LiveData<List<Breed>> = _breedList

    init {
        getDogBreed()
    }

    suspend fun loadImage(image: File, str: HashMap<String, RequestBody>) {

        // usecase에서 데이터 변환
        // usecase에 집어넣기
        dogProfileRepository.getDogProfilePhoto(image, str)
    }

    suspend fun postRegisterInfo(img: File, data: RegisterInfo) =
        postRegisterUseCase(img, data)


    fun getDogBreed2() = getDogBreedUseCase2()


    //초기화해서 접근? 아니면 함수 반환? -> 초기화블록에서 실행해야 바텀시트 연속 띄우기 해결됨
    private fun getDogBreed() {
        viewModelScope.launch {
            _breedList.value = getDogBreedUseCase().data
        }
    }

    fun getDogAge() = getDogAgeUseCase()
}