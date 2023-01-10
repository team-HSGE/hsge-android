package com.starters.hsge.presentation.main.mypage.mypage

import com.starters.hsge.domain.model.UserInfo

sealed class MyPageState {
    object Initial : MyPageState()
    object Loading : MyPageState()
    class Failure(val msg: Throwable) : MyPageState()
    class Success(val data: UserInfo) : MyPageState()
}