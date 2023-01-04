package com.starters.hsge.data.repository

import com.starters.hsge.data.api.SignUpApi
import com.starters.hsge.data.model.remote.response.SignUpResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.domain.repository.RegisterRepository
import com.starters.hsge.domain.util.FormDataUtil
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val signUpApi: SignUpApi
) : RegisterRepository {
    override suspend fun postUserData(img: File, data: RegisterInfo): Response<SignUpResponse> {
        val formJson = Json.encodeToString(data)
        val formMultipart = FormDataUtil.getImageBody("imgFile", img)
        val formRequestBody = FormDataUtil.getJsonBody(formJson)
        val registerInfoHashMap = hashMapOf<String, RequestBody>()
        registerInfoHashMap["signupDto"] = formRequestBody

        return signUpApi.postSignUp(formMultipart, registerInfoHashMap)
    }
}