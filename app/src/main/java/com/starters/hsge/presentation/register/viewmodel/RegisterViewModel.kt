package com.starters.hsge.presentation.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.response.Age
import com.starters.hsge.data.model.remote.response.Breed
import com.starters.hsge.domain.model.RegisterInfo
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

    private val _ageList = MutableLiveData<List<Age>>()
    val ageList: LiveData<List<Age>> = _ageList

    init {
        getDogBreed()
        getDogAge()
    }

    suspend fun postRegisterInfo(img: File, data: RegisterInfo) = postRegisterUseCase(img, data)

    private fun getDogBreed() {
        viewModelScope.launch {
            _breedList.value = getDogBreedUseCase().data
        }
    }

    private fun getDogAge() {
        viewModelScope.launch {
            _ageList.value = getDogAgeUseCase().data
        }
    }
}