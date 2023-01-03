package com.starters.hsge.data.repository

import com.starters.hsge.data.api.UserDogApi
import com.starters.hsge.data.model.remote.request.EditDogProfileRequest
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.FormDataUtil
import com.starters.hsge.domain.repository.EditDogProfileRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

const val CONTENT_KEY = "content"

class EditDogProfileRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: UserDogApi
) : EditDogProfileRepository {
    override suspend fun putDogData(
        petId: Int,
        img: File?,
        data: EditDogProfileRequest
    ) {
        val formJson = Json.encodeToString(data)
        val formMultipart = img?.let { FormDataUtil.getImageBody("imgFile", it) }
        val formRequestBody = FormDataUtil.getJsonBody(formJson)
        val dogInfoHashMap = hashMapOf<String, RequestBody>()
        dogInfoHashMap[CONTENT_KEY] = formRequestBody
        api.postEditDog(petId, formMultipart, dogInfoHashMap)
    }
}
