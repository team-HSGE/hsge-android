package com.starters.hsge.data.repository

import com.starters.hsge.data.api.EditLocationApi
import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.EditLocationRepository
import javax.inject.Inject

class EditLocationRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: EditLocationApi
) : EditLocationRepository {
    override suspend fun putLocation(location: UserLocationRequest) {
        api.putLocationData(location)
    }
}