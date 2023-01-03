package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.request.UserLocationRequest

interface UserRepository {
    suspend fun putLocation(location: UserLocationRequest)
}