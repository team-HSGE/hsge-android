package com.starters.hsge.domain.repository

import com.starters.hsge.ApiResult
import com.starters.hsge.data.model.remote.response.SignUpResponse
import com.starters.hsge.domain.model.RegisterInfo
import kotlinx.coroutines.flow.Flow
import java.io.File

interface RegisterRepository {
    suspend fun postUserData(img: File, data: RegisterInfo): Flow<ApiResult<SignUpResponse>>
}