package com.starters.hsge.data.repository

import com.starters.hsge.data.api.UserApi
import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.UserRepository
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: UserApi
) : UserRepository {
    override suspend fun putLocation(location: UserLocationRequest): Response<Void> {
        return api.putLocationData(location)
    }
}