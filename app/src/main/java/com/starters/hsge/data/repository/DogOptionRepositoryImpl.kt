package com.starters.hsge.data.repository

import com.starters.hsge.common.constants.DislikeTag
import com.starters.hsge.common.constants.LikeTag
import com.starters.hsge.data.api.DogOptionApi
import com.starters.hsge.data.model.remote.response.DogAgeResponse
import com.starters.hsge.data.model.remote.response.DogBreedResponse
import com.starters.hsge.di.RetrofitHSGE
import com.starters.hsge.domain.repository.DogOptionRepository
import javax.inject.Inject

class DogOptionRepositoryImpl @Inject constructor(
    @RetrofitHSGE private val dogOptionApi: DogOptionApi
) : DogOptionRepository {

    override suspend fun getDogBreed(): DogBreedResponse {
        return dogOptionApi.getBreed()
    }

    override suspend fun getDogAge(): DogAgeResponse {
        return dogOptionApi.getAge()
    }

    override fun getDogLikeTags(): List<String> = listOf(
        LikeTag.MAN.tag,
        LikeTag.WOMAN.tag,
        LikeTag.CHILD.tag,
        LikeTag.HUMAN.tag,
        LikeTag.MALE.tag,
        LikeTag.FEMALE.tag,
        LikeTag.BALL_PLAY.tag,
        LikeTag.TUG_PLAY.tag,
        LikeTag.WALK.tag,
        LikeTag.SWIM.tag,
        LikeTag.LARGE_DOG.tag,
        LikeTag.MEDIUM_DOG.tag,
        LikeTag.SMALL_DOG.tag,
        LikeTag.DRESSING.tag,
        LikeTag.PICTURE.tag,
        LikeTag.SLEEPING.tag,
        LikeTag.TREAT.tag,
        LikeTag.SWEET_POTATO.tag,
        LikeTag.CHICKEN_BREAST.tag,
        LikeTag.VEGETABLE.tag,
        LikeTag.FRUIT.tag,
        LikeTag.SWEET_PUMPKIN.tag,
        LikeTag.DOG_CHEW.tag,
        LikeTag.DOLL.tag
    )

    override fun getDogDislikeTags(): List<String> = listOf(
        DislikeTag.MAN.tag,
        DislikeTag.WOMAN.tag,
        DislikeTag.CHILD.tag,
        DislikeTag.HUMAN.tag,
        DislikeTag.MALE.tag,
        DislikeTag.FEMALE.tag,
        DislikeTag.LARGE_DOG.tag,
        DislikeTag.MEDIUM_DOG.tag,
        DislikeTag.SMALL_DOG.tag,
        DislikeTag.DRESSING.tag,
        DislikeTag.PICTURE.tag,
        DislikeTag.SWIM.tag,
        DislikeTag.KISS.tag,
        DislikeTag.TOUCH_PAW.tag,
        DislikeTag.TOUCH_TAIL.tag,
        DislikeTag.TOUCH.tag,
        DislikeTag.LOUD_NOISE.tag,
        DislikeTag.PERFUME.tag,
        DislikeTag.NOTHING.tag
    )
}
