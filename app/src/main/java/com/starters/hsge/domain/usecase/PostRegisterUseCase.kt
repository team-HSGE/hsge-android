package com.starters.hsge.domain.usecase

import android.net.Uri
import androidx.core.net.toFile
import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.domain.repository.RegisterRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PostRegisterUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend operator fun invoke(img: Uri, data: RegisterInfo) {
        val formFile = img.toFile()
        val formJson = Json.encodeToString(data)
        registerRepository.postUserData(formFile, formJson)
    }
}