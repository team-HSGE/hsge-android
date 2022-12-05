package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DogBreedResponse(
    @SerialName("data")
    val data: List<Breed>,
    @SerialName("message")
    val message: String
)

@Serializable
data class Breed(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String
)
