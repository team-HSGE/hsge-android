package com.starters.hsge.presentation.register.viewmodel

import com.starters.hsge.domain.repository.DogProfileRepository
import com.starters.hsge.domain.usecase.GetDogBreedUseCase
import com.starters.hsge.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dogProfileRepository: DogProfileRepository,
    private val getDogBreedUseCase: GetDogBreedUseCase
) : BaseViewModel() {

    suspend fun loadImage(image: File) {
        dogProfileRepository.getDogProfilePhoto(image)
    }

    fun getDogBreed() = getDogBreedUseCase()
}