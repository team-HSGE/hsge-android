package com.starters.hsge.common.constants

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DogBreedServer(
    @SerialName("data")
    val data: List<Data>,
    @SerialName("message")
    val message: String
)