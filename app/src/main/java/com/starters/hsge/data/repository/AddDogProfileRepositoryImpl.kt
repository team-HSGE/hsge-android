package com.starters.hsge.data.repository

import com.starters.hsge.data.api.UserDogApi
import com.starters.hsge.data.model.remote.request.AddDogRequest
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.FormDataUtil
import com.starters.hsge.domain.repository.AddDogProfileRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

const val SIGN_UP_DTO_KEY = "signupDto"
const val IMAGE_FILE_KEY = "imgFile"

class AddDogProfileRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: UserDogApi
) : AddDogProfileRepository {
    override suspend fun postDogData(img: File, data: AddDogRequest) {
        val formJson = Json.encodeToString(data)
        val formMultipart = FormDataUtil.getImageBody(IMAGE_FILE_KEY, img)
        val formRequestBody = FormDataUtil.getJsonBody(formJson)
        val dogInfoHashMap = hashMapOf<String, RequestBody>()
        dogInfoHashMap[SIGN_UP_DTO_KEY] = formRequestBody
        api.postDogProfile(formMultipart, dogInfoHashMap)
    }

}