package com.starters.hsge.data.repository

import com.starters.hsge.data.api.UserDogApi
import com.starters.hsge.data.model.remote.request.AddDogRequest
import com.starters.hsge.data.model.remote.request.EditDogRequest
import com.starters.hsge.data.model.remote.response.UserDogResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.util.FormDataUtil
import com.starters.hsge.domain.repository.UserDogRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

const val SIGN_UP_DTO_KEY = "signupDto"
const val IMAGE_FILE_KEY = "imgFile"
const val CONTENT_KEY = "content"

class UserDogRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: UserDogApi
) : UserDogRepository {

    override suspend fun getMyDog(): List<UserDogResponse> {
        return api.getMyDog()
    }

    override suspend fun putDogData(
        petId: Int,
        img: File?,
        data: EditDogRequest
    ) {
        val formJson = Json.encodeToString(data)
        val formMultipart = img?.let { FormDataUtil.getImageBody("imgFile", it) }
        val formRequestBody = FormDataUtil.getJsonBody(formJson)
        val dogInfoHashMap = hashMapOf<String, RequestBody>()
        dogInfoHashMap[CONTENT_KEY] = formRequestBody
        api.postEditDog(petId, formMultipart, dogInfoHashMap)
    }

    override suspend fun postDogData(img: File, data: AddDogRequest) {
        val formJson = Json.encodeToString(data)
        val formMultipart = FormDataUtil.getImageBody(IMAGE_FILE_KEY, img)
        val formRequestBody = FormDataUtil.getJsonBody(formJson)
        val dogInfoHashMap = hashMapOf<String, RequestBody>()
        dogInfoHashMap[SIGN_UP_DTO_KEY] = formRequestBody
        api.postDogProfile(formMultipart, dogInfoHashMap)
    }

    override suspend fun deleteDogProfile(petId: Int) {
        api.deleteDog(petId)
    }
}