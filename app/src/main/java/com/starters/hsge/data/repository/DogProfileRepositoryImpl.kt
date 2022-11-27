package com.starters.hsge.data.repository

import com.starters.hsge.common.constants.DogAge
import com.starters.hsge.common.constants.DogBreed
import com.starters.hsge.data.api.ImageService
import com.starters.hsge.di.RetrofitBase
import com.starters.hsge.domain.FormDataUtil
import com.starters.hsge.domain.repository.DogProfileRepository
import java.io.File
import javax.inject.Inject

class DogProfileRepositoryImpl @Inject constructor(
    @RetrofitBase private val api: ImageService
) : DogProfileRepository {
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

    override fun getDogAge(): List<DogAge> = listOf(
        DogAge.ONE_MONTH_LESS,
        DogAge.ONE_MONTH,
        DogAge.TWO_MONTHS,
        DogAge.THREE_MONTHS,
        DogAge.FOUR_MONTHS,
        DogAge.FIVE_MONTHS,
        DogAge.SIX_MONTHS,
        DogAge.SEVEN_MONTHS,
        DogAge.EIGHT_MONTHS,
        DogAge.NINE_MONTHS,
        DogAge.TEN_MONTHS,
        DogAge.ELEVEN_MONTHS,
        DogAge.ONE_YEAR,
        DogAge.TWO_YEARS,
        DogAge.THREE_YEARS,
        DogAge.FOUR_YEARS,
        DogAge.FIVE_YEARS,
        DogAge.SIX_YEARS,
        DogAge.SEVEN_YEARS,
        DogAge.EIGHT_YEARS,
        DogAge.NINE_YEARS,
        DogAge.TEN_YEARS,
        DogAge.ELEVEN_YEARS,
        DogAge.TWELVE_YEARS,
        DogAge.THIRTEEN_YEARS,
        DogAge.FOURTEEN_YEARS,
        DogAge.FIFTEEN_YEARS,
        DogAge.SIXTEEN_YEARS,
        DogAge.SEVENTEEN_YEARS,
        DogAge.EIGHTEEN_YEARS,
        DogAge.NINETEEN_YEARS,
        DogAge.TWENTY_YEARS,
        DogAge.TWENTY_YEARS_MORE
    )

    override fun getDogBreed(): List<DogBreed> = listOf(
        DogBreed.MIX,
        DogBreed.RETRIEVER,
        DogBreed.DACHSHUND,
        DogBreed.MALTESE,
        DogBreed.SCHNAUZER,
        DogBreed.STANDARD_POODLE,
        DogBreed.TOY_POODLE,
        DogBreed.MINIATURE_PINSCHER,
        DogBreed.BERLINGTON_TERRIER,
        DogBreed.BORDER_COLLIE,
        DogBreed.BOSTON_TERRIER,
        DogBreed.BEAGLE,
        DogBreed.BICHON_FRISE,
        DogBreed.SAMOYED,
        DogBreed.SHIBA_INU,
        DogBreed.SIBERIAN_HUSKY,
        DogBreed.SHIH_TZU,
        DogBreed.COCKER_SPANIEL,
        DogBreed.YORKSHIRE_TERRIER,
        DogBreed.WELSH_CORGI,
        DogBreed.ITALIAN_GREYHOUND,
        DogBreed.SPITZ,
        DogBreed.JINDO,
        DogBreed.CHIHUAHUA,
        DogBreed.PAPILLON,
        DogBreed.PUG,
        DogBreed.POMERANIAN,
        DogBreed.FRENCH_BULLDOG
    )

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