package com.starters.hsge.domain.mapper

import com.starters.hsge.data.model.remote.response.DogBreedResponse

fun DogBreedResponse.mapToBreedMap(): Map<String, String> {
    val breedMap = mutableMapOf<String, String>()

    this.data.map {
        breedMap.put(
            it.value,
            it.key
        )
    }
    return breedMap
}