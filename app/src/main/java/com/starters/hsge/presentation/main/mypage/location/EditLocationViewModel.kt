package com.starters.hsge.presentation.main.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.domain.usecase.PutEditLocationUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EditLocationViewModel @Inject constructor(
    private val putEditLocationUseCase: PutEditLocationUseCase
) : BaseViewModel() {

    val mResponse: MutableLiveData<Response<Void>> = MutableLiveData()

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var town: String = ""

    fun putLocation(location: UserLocationRequest) {
        viewModelScope.launch {
            val response = putEditLocationUseCase(location)
            mResponse.value = response
        }
    }
}