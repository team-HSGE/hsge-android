package com.starters.hsge.data.model.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DogBreedResponse(
    @SerialName("data")
    val data: List<Data>,
    @SerialName("message")
    val message: String
)

@Serializable
data class Data(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String
)