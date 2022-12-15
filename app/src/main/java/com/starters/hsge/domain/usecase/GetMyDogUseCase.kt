package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.repository.MyDogRepository
import javax.inject.Inject

class GetMyDogUseCase @Inject constructor(
    private val myDogRepository: MyDogRepository
) {
    suspend operator fun invoke() =  myDogRepository.getMyDog()
}