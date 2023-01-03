package com.starters.hsge.presentation.main.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.response.UserDogResponse
import com.starters.hsge.domain.usecase.GetMyDogUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val getMyDogUseCase: GetMyDogUseCase
) : BaseViewModel() {

    private val _myDogList = MutableLiveData<List<UserDogResponse>?>()
    val myDogList:LiveData<List<UserDogResponse>?> =  _myDogList

    init {
        getMyDogList()
    }

    private fun getMyDogList() {
        viewModelScope.launch {
            _myDogList.value = getMyDogUseCase()
        }
    }

}