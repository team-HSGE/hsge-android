package com.starters.hsge.data.repository

import com.starters.hsge.data.api.EditDogApi
import com.starters.hsge.data.model.remote.request.EditDogProfileRequest
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.FormDataUtil
import com.starters.hsge.domain.repository.EditDogProfileRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class EditDogProfileRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: EditDogApi
) : EditDogProfileRepository {
    override suspend fun postDogData(
        petId: Int,
        img: File,
        data: EditDogProfileRequest
    ) {
        val formJson = Json.encodeToString(data)
        val formMultipart = FormDataUtil.getImageBody("imgFile", img)
        val formRequestBody = FormDataUtil.getJsonBody(formJson)
        val dogInfoHashMap = hashMapOf<String, RequestBody>()
        dogInfoHashMap["content"] = formRequestBody
        api.postEditDog(petId, formMultipart, dogInfoHashMap)
    }
}