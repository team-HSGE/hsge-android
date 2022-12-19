package com.starters.hsge.presentation.main.add

import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddDogProfileViewModel @Inject constructor() : BaseViewModel() {

    var dogPhoto = ""
    var dogName = ""
    var dogSex = ""
    var dogNeuter = false
    var dogAge = ""
    var dogBreed = ""
    var dogLikeTag = listOf<String>()
    var dogDislikeTag = listOf<String>()
    var dogLikeTagStr = ""
    var dogDislikeTagStr = ""
    var dogDescription = ""
}