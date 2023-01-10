package com.starters.hsge.presentation.main.mypage.management

import com.starters.hsge.data.model.remote.response.UserDogResponse

sealed class ManagementState {
    object Initial: ManagementState()
    object Loading: ManagementState()
    class Failure(val msg: Throwable) : ManagementState()
    class Success(val data: List<UserDogResponse>) : ManagementState()
}