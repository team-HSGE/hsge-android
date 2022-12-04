package com.starters.hsge.data.repository

import com.starters.hsge.data.api.UserApi
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.FormDataUtil
import com.starters.hsge.domain.repository.RegisterRepository
import java.io.File
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val userApi: UserApi
) : RegisterRepository {
    override suspend fun postUserData(img: File, data: String) {
        val formMultipart = FormDataUtil.getImageBody("imgFile", img)
        val formRequestBody = FormDataUtil.getJsonBody("signUpDto", data)
        userApi.postSignUp(formMultipart, formRequestBody)
    }
}