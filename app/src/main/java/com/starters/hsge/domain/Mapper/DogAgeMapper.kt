package com.starters.hsge.domain.Mapper

import com.starters.hsge.data.model.remote.response.DogAgeResponse

fun DogAgeResponse.mapToAgeMap(): Map<String, String> {
    val ageMap = mutableMapOf<String, String>()

    this.data.map {
        ageMap.put(
            it.value,
            it.key
        )
    }
    return ageMap
}