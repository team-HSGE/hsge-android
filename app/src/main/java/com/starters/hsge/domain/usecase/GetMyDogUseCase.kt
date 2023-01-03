package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.UserDogRepository
import javax.inject.Inject

class GetMyDogUseCase @Inject constructor(
    private val userDogRepository: UserDogRepository
) {
    suspend operator fun invoke() =  userDogRepository.getMyDog()
}