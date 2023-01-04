package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.request.UserLocationRequest
import retrofit2.Response

interface UserRepository {
    suspend fun putLocation(location: UserLocationRequest): Response<Void>
}