package com.starters.hsge.data.interfaces

import com.starters.hsge.data.model.remote.response.DogCard

interface HomeDogInterface {

    fun onGetHomeDogSuccess(DogCardResponse: List<DogCard>?, isSuccess: Boolean, code: Int)

    fun onGetHomeDogFailure(message: String)
}