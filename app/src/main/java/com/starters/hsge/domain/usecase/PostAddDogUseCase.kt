package com.starters.hsge.domain.usecase

import com.starters.hsge.data.model.remote.request.AddDogRequest
import com.starters.hsge.domain.repository.UserDogRepository
import java.io.File
import javax.inject.Inject

class PostAddDogUseCase @Inject constructor(
    private val userDogRepository: UserDogRepository
) {
    suspend operator fun invoke(img: File, data: AddDogRequest ) =
        userDogRepository.postDogData(img, data)
}