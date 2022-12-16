package com.starters.hsge.domain.repository

import com.starters.hsge.data.model.remote.request.UserLocationRequest

interface EditLocationRepository {
    suspend fun putLocation(location: UserLocationRequest)
}