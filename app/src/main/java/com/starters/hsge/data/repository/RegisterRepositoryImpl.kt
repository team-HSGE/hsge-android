package com.starters.hsge.data.repository

import com.starters.hsge.data.api.UserApi
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.FormDataUtil
import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.domain.repository.RegisterRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val userApi: UserApi
) : RegisterRepository {
    override suspend fun postUserData(img: File, data: RegisterInfo) {
        val formJson = Json.encodeToString(data)
        val formMultipart = FormDataUtil.getImageBody("imgFile", img)
        val formRequestBody = FormDataUtil.getJsonBody(formJson)
        val registerInfoHashMap = hashMapOf<String, RequestBody>()
        registerInfoHashMap["signupDto"] = formRequestBody
        userApi.postSignUp(formMultipart, registerInfoHashMap)
    }
}