package com.starters.hsge.domain.repository

import com.starters.hsge.common.constants.UserIcon
import com.starters.hsge.data.model.remote.request.EditUserRequest
import com.starters.hsge.data.model.remote.request.UserLocationRequest
import com.starters.hsge.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface UserRepository {

    suspend fun getUserInfo(): Flow<UserInfo>

    suspend fun putUserInfo(userInfo: EditUserRequest): Response<Void>

    suspend fun putLocation(location: UserLocationRequest): Response<Void>

    fun getIcons(): List<UserIcon>
}