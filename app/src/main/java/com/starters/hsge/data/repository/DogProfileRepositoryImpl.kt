package com.starters.hsge.data.repository

import com.starters.hsge.common.constants.DogAge
import com.starters.hsge.common.constants.DogBreedLocal
import com.starters.hsge.data.api.ImageService
import com.starters.hsge.di.RetrofitBase
import com.starters.hsge.domain.FormDataUtil
import com.starters.hsge.domain.repository.DogProfileRepository
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class DogProfileRepositoryImpl @Inject constructor(
    @RetrofitBase private val api: ImageService
) : DogProfileRepository {
    override suspend fun getDogProfilePhoto(image: File, str: HashMap<String, RequestBody>) {
        val formFile = FormDataUtil.getImageBody("imgFile", image)
        api.uploadImage(formFile, str)
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

    override fun getDogBreed(): List<DogBreedLocal> = listOf(
        DogBreedLocal.MIX,
        DogBreedLocal.RETRIEVER,
        DogBreedLocal.DACHSHUND,
        DogBreedLocal.MALTESE,
        DogBreedLocal.SCHNAUZER,
        DogBreedLocal.STANDARD_POODLE,
        DogBreedLocal.TOY_POODLE,
        DogBreedLocal.MINIATURE_PINSCHER,
        DogBreedLocal.BERLINGTON_TERRIER,
        DogBreedLocal.BORDER_COLLIE,
        DogBreedLocal.BOSTON_TERRIER,
        DogBreedLocal.BEAGLE,
        DogBreedLocal.BICHON_FRISE,
        DogBreedLocal.SAMOYED,
        DogBreedLocal.SHIBA_INU,
        DogBreedLocal.SIBERIAN_HUSKY,
        DogBreedLocal.SHIH_TZU,
        DogBreedLocal.COCKER_SPANIEL,
        DogBreedLocal.YORKSHIRE_TERRIER,
        DogBreedLocal.WELSH_CORGI,
        DogBreedLocal.ITALIAN_GREYHOUND,
        DogBreedLocal.SPITZ,
        DogBreedLocal.JINDO,
        DogBreedLocal.CHIHUAHUA,
        DogBreedLocal.PAPILLON,
        DogBreedLocal.PUG,
        DogBreedLocal.POMERANIAN,
        DogBreedLocal.FRENCH_BULLDOG,
        DogBreedLocal.ETC
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