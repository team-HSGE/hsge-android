package com.starters.hsge.domain.usecase

import com.starters.hsge.data.model.remote.request.EditDogRequest
import com.starters.hsge.domain.repository.UserDogRepository
import java.io.File
import javax.inject.Inject

class PutEditDogProfileUseCase @Inject constructor(
    private val userDogRepository: UserDogRepository
) {
    suspend operator fun invoke(petId: Int, img: File?, data: EditDogRequest ) =
        userDogRepository.putDogData(petId, img, data)
}