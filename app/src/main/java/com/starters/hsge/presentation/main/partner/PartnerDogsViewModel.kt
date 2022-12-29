package com.starters.hsge.presentation.main.partner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.response.MyDogResponse
import com.starters.hsge.domain.usecase.GetPartnerInfoUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartnerDogsViewModel @Inject constructor(
    private val getPartnerInfoUseCase: GetPartnerInfoUseCase
): BaseViewModel() {

    private val _partnerDogList = MutableLiveData<List<MyDogResponse>?>()
    val partnerDogList: LiveData<List<MyDogResponse>?> =  _partnerDogList

    var partnerNickName = ""

    fun getPartnerDogs(partnerId: Long): LiveData<List<MyDogResponse>?> {
        viewModelScope.launch {
           _partnerDogList.value = getPartnerInfoUseCase(partnerId)
        }
        return partnerDogList
    }
}