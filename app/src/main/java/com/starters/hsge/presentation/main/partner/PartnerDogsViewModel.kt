package com.starters.hsge.presentation.main.partner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.response.UserDogResponse
import com.starters.hsge.domain.usecase.GetPartnerUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerDogsViewModel @Inject constructor(
    private val getPartnerUseCase: GetPartnerUseCase
): BaseViewModel() {

    private val _partnerDogList = MutableLiveData<List<UserDogResponse>?>()
    val partnerDogList: LiveData<List<UserDogResponse>?> =  _partnerDogList

    var partnerNickName = ""

    fun getPartnerDogs(partnerId: Long): LiveData<List<UserDogResponse>?> {
        viewModelScope.launch {
           _partnerDogList.value = getPartnerUseCase(partnerId)
        }
        return partnerDogList
    }
}