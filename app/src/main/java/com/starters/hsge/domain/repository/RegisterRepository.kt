package com.starters.hsge.domain.repository

import com.starters.hsge.domain.model.RegisterInfo
import java.io.File

interface RegisterRepository {
    suspend fun postUserData(img: File, data: RegisterInfo)
}