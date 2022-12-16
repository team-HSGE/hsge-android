package com.starters.hsge.presentation.main.edit

import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.request.EditDogProfileRequest
import com.starters.hsge.domain.usecase.PutEditDogProfileUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DogProfileEditViewModel @Inject constructor(
    private val putEditDogProfileUseCase: PutEditDogProfileUseCase,
) : BaseViewModel() {

    var dogName = ""
    var dogSex = ""
    var dogNeuter = false
    var dogAge = ""
    var dogBreed = ""
    var dogLikeTag = ""
    var dogDislikeTag = ""
    var description = ""
    var dogPhoto: String = ""

    private val _likeTags: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val likeTags: StateFlow<List<String>>
        get() = _likeTags

    fun fetchLikeTags() {
        viewModelScope.launch {
            _likeTags.value
        }
    }

    fun putEditDogProfile(petId: Int, img: File, data: EditDogProfileRequest ) {
        viewModelScope.launch {
            putEditDogProfileUseCase(petId, img, data)
        }
    }
}