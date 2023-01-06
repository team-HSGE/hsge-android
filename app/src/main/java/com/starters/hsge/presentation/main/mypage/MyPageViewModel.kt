package com.starters.hsge.presentation.main.mypage

import androidx.lifecycle.viewModelScope
import com.starters.hsge.domain.usecase.GetUserInfoUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : BaseViewModel() {

    private val _userInfo: MutableStateFlow<MyPageState> =
        MutableStateFlow(MyPageState.Initial)
    val userInfo: StateFlow<MyPageState>
        get() = _userInfo

    fun getUserInfo() {
        viewModelScope.launch {
            _userInfo.value = MyPageState.Loading
            getUserInfoUseCase.invoke().catch { e ->
                Timber.d("Failure $e")
                _userInfo.value = MyPageState.Failure(e)
            }.collect {
                _userInfo.value = MyPageState.Success(it)
            }
        }
    }
}