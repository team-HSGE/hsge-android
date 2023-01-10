package com.starters.hsge.data.repository

import com.starters.hsge.data.api.SignUpApi
import com.starters.hsge.data.model.remote.response.SignUpResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.model.RegisterInfo
import com.starters.hsge.domain.repository.RegisterRepository
import com.starters.hsge.domain.util.FormDataUtil
import com.starters.hsge.presentation.common.constants.IMAGE_FILE_KEY
import com.starters.hsge.presentation.common.constants.SIGN_UP_DTO_KEY
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
        val formMultipart = FormDataUtil.getImageBody(IMAGE_FILE_KEY, img)
        val formRequestBody = FormDataUtil.getJsonBody(formJson)
        val registerInfoHashMap = hashMapOf<String, RequestBody>()
        registerInfoHashMap[SIGN_UP_DTO_KEY] = formRequestBody

        return signUpApi.postSignUp(formMultipart, registerInfoHashMap)
    }
}