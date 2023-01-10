package com.starters.hsge.presentation.main.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.starters.hsge.data.model.remote.request.EditUserRequest
import com.starters.hsge.domain.usecase.PutEditUserProfileUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserProfileEditViewModel @Inject constructor(
    private val putEditUserProfileUseCase: PutEditUserProfileUseCase
): BaseViewModel() {

    val mResponse: MutableLiveData<Response<Void>> = MutableLiveData()

    var email = ""
    var nickName = ""
    var userIcon = 0

    fun putUserInfo(userInfo: EditUserRequest) {
        viewModelScope.launch {
            val response = putEditUserProfileUseCase(userInfo)
            mResponse.value = response
        }
    }
}
