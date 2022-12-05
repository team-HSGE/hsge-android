package com.starters.hsge.domain.usecase

import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.domain.repository.RegisterRepository
import java.io.File
import javax.inject.Inject

class PostRegisterUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend operator fun invoke(img: File, data: RegisterInfo) =
        registerRepository.postUserData(img, data)
}