package com.starters.hsge.domain.repository

import java.io.File

interface RegisterRepository {
    suspend fun postUserData(img: File, data: String)
}