package com.starters.hsge.data.repository

import com.starters.hsge.data.api.ImageService
import com.starters.hsge.di.RetrofitBase
import com.starters.hsge.domain.FormDataUtil
import com.starters.hsge.domain.repository.DogProfileRepository
import java.io.File
import javax.inject.Inject

class DogProfileRepositoryImpl @Inject constructor(
    @RetrofitBase private val api: ImageService
): DogProfileRepository {
    override suspend fun getDogProfilePhoto(image: File) {
        val formFile = FormDataUtil.getImageBody("imgFile", image)
        api.uploadImage(formFile)
    }

    override suspend fun getDogName() {
        TODO("Not yet implemented")
    }

    override suspend fun getDogSex() {
        TODO("Not yet implemented")
    }

    override suspend fun getDogNeuter() {
        TODO("Not yet implemented")
    }

    override suspend fun getDogAge() {
        TODO("Not yet implemented")
    }

    override suspend fun getDogBreed() {
        TODO("Not yet implemented")
    }

    override suspend fun getDogLikeTag() {
        TODO("Not yet implemented")
    }

    override suspend fun getDogDislikeTag() {
        TODO("Not yet implemented")
    }

    override suspend fun postDogProfilePhoto() {
        TODO("Not yet implemented")
    }

    override suspend fun postDogName() {
        TODO("Not yet implemented")
    }

    override suspend fun postDogSex() {
        TODO("Not yet implemented")
    }

    override suspend fun postDogNeuter() {
        TODO("Not yet implemented")
    }

    override suspend fun postDogAge() {
        TODO("Not yet implemented")
    }

    override suspend fun postDogBreed() {
        TODO("Not yet implemented")
    }

    override suspend fun postDogLikeTag() {
        TODO("Not yet implemented")
    }

    override suspend fun postDogDislikeTag() {
        TODO("Not yet implemented")
    }
}