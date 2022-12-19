package com.starters.hsge.domain.usecase

import com.starters.hsge.data.model.remote.request.EditDogProfileRequest
import com.starters.hsge.domain.repository.EditDogProfileRepository
import java.io.File
import javax.inject.Inject

class PutEditDogProfileUseCase @Inject constructor(
    private val editDogProfileRepository: EditDogProfileRepository
) {
    suspend operator fun invoke(petId: Int, img: File?, data: EditDogProfileRequest ) =
        editDogProfileRepository.postDogData(petId, img, data)
}