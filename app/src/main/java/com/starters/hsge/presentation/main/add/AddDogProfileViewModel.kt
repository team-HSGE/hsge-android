package com.starters.hsge.presentation.main.add

import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.request.AddDogRequest
import com.starters.hsge.domain.usecase.PostAddDogUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddDogProfileViewModel @Inject constructor(
    private val postAddDogUseCase: PostAddDogUseCase
) : BaseViewModel() {

    var dogPhoto = ""
    var dogSex = ""
    var dogAge = ""
    var dogBreed = ""
    var dogLikeTag = listOf<String>()
    var dogDislikeTag = listOf<String>()
    var dogLikeTagStr = ""
    var dogDislikeTagStr = ""

    fun postDogProfile(img: File, data: AddDogRequest) {
        viewModelScope.launch {
            postAddDogUseCase(img, data)
        }
    }
}