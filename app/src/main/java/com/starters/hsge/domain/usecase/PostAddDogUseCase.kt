package com.starters.hsge.domain.usecase

import com.starters.hsge.data.model.remote.request.AddDogRequest
import com.starters.hsge.domain.repository.AddDogProfileRepository
import java.io.File
import javax.inject.Inject

class PostAddDogUseCase @Inject constructor(
    private val addDogProfileRepository: AddDogProfileRepository
) {
    suspend operator fun invoke(img: File, data: AddDogRequest ) =
        addDogProfileRepository.postDogData(img, data)
}