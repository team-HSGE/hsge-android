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

    override fun getDogLikeTags(): List<LikeTag> = listOf(
        LikeTag.MAN,
        LikeTag.WOMAN,
        LikeTag.CHILD,
        LikeTag.HUMAN,
        LikeTag.MALE,
        LikeTag.FEMALE,
        LikeTag.BALL_PLAY,
        LikeTag.TUG_PLAY,
        LikeTag.WALK,
        LikeTag.SWIM,
        LikeTag.LARGE_DOG,
        LikeTag.MEDIUM_DOG,
        LikeTag.SMALL_DOG,
        LikeTag.DRESSING,
        LikeTag.PICTURE,
        LikeTag.SLEEPING,
        LikeTag.TREAT,
        LikeTag.SWEET_POTATO,
        LikeTag.CHICKEN_BREAST,
        LikeTag.VEGETABLE,
        LikeTag.FRUIT,
        LikeTag.SWEET_PUMPKIN,
        LikeTag.DOG_CHEW,
        LikeTag.DOLL
    )

    override fun getDogDislikeTags(): List<DislikeTag> = listOf(
        DislikeTag.MAN,
        DislikeTag.WOMAN,
        DislikeTag.CHILD,
        DislikeTag.HUMAN,
        DislikeTag.MALE,
        DislikeTag.FEMALE,
        DislikeTag.LARGE_DOG,
        DislikeTag.MEDIUM_DOG,
        DislikeTag.SMALL_DOG,
        DislikeTag.DRESSING,
        DislikeTag.PICTURE,
        DislikeTag.SWIM,
        DislikeTag.KISS,
        DislikeTag.TOUCH_PAW,
        DislikeTag.TOUCH_TAIL,
        DislikeTag.TOUCH,
        DislikeTag.LOUD_NOISE,
        DislikeTag.PERFUME
    )
}
