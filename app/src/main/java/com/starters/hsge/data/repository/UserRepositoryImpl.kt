package com.starters.hsge.data.repository

import com.starters.hsge.data.api.UserApi
import com.starters.hsge.data.model.remote.request.EditUserRequest
import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.mapper.mapToUserInfo
import com.starters.hsge.domain.model.UserInfo
import com.starters.hsge.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: UserApi
) : UserRepository {
    override suspend fun getUserInfo(): Flow<UserInfo> =
        flow {
            emit(api.getUserInfo().mapToUserInfo())
        }

    override suspend fun putUserInfo(userInfo: EditUserRequest): Response<Void> {
        return api.putUserInfo(userInfo)
    }

    override suspend fun putLocation(location: UserLocationRequest): Response<Void> {
        return api.putLocationData(location)
    }
}