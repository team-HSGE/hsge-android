package com.starters.hsge.presentation.main.management

import androidx.lifecycle.viewModelScope
import com.starters.hsge.domain.usecase.GetMyDogUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
    private val getMyDogUseCase: GetMyDogUseCase,
) : BaseViewModel() {

    private val _myDogList: MutableStateFlow<ManagementState> =
        MutableStateFlow(ManagementState.Initial)
    val myDogList: StateFlow<ManagementState>
        get() = _myDogList

    fun getMyDogList() {
        viewModelScope.launch {
            _myDogList.value = ManagementState.Loading
            getMyDogUseCase.invoke().catch { e ->
                _myDogList.value = ManagementState.Failure(e)
            }.collect {
                _myDogList.value = ManagementState.Success(it)
            }
        }
    }
}