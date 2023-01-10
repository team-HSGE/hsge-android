package com.starters.hsge.data.repository

import com.starters.hsge.data.api.UserDogApi
import com.starters.hsge.data.model.remote.request.AddDogRequest
import com.starters.hsge.data.model.remote.request.EditDogRequest
import com.starters.hsge.data.model.remote.response.UserDogResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.util.FormDataUtil
import com.starters.hsge.domain.repository.UserDogRepository
import com.starters.hsge.presentation.common.constants.CONTENT_KEY
import com.starters.hsge.presentation.common.constants.IMAGE_FILE_KEY
import com.starters.hsge.presentation.common.constants.SIGN_UP_DTO_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class UserDogRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val api: UserDogApi
) : UserDogRepository {

    override suspend fun getMyDog(): Flow<List<UserDogResponse>> =
        flow {
            emit(api.getMyDog())
        }

    override suspend fun putDogData(
        petId: Int,
        img: File?,
        data: EditDogRequest
    ): Response<Void> {
        val formJson = Json.encodeToString(data)
        val formMultipart = img?.let { FormDataUtil.getImageBody("imgFile", it) }
        val formRequestBody = FormDataUtil.getJsonBody(formJson)
        val dogInfoHashMap = hashMapOf<String, RequestBody>()
        dogInfoHashMap[CONTENT_KEY] = formRequestBody
        return api.postEditDog(petId, formMultipart, dogInfoHashMap)
    }

    override suspend fun postDogData(img: File, data: AddDogRequest): Response<Void> {
        val formJson = Json.encodeToString(data)
        val formMultipart = FormDataUtil.getImageBody(IMAGE_FILE_KEY, img)
        val formRequestBody = FormDataUtil.getJsonBody(formJson)
        val dogInfoHashMap = hashMapOf<String, RequestBody>()
        dogInfoHashMap[SIGN_UP_DTO_KEY] = formRequestBody
        return api.postDogProfile(formMultipart, dogInfoHashMap)
    }

    override suspend fun deleteDogProfile(petId: Int): Response<Void> {
        return api.deleteDog(petId)
    }
}