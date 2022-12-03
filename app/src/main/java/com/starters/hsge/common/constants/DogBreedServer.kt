package com.starters.hsge.common.constants

import com.starters.hsge.data.model.Data
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DogBreedServer(
    @SerialName("data")
    val data: List<Data>,
    @SerialName("message")
    val message: String
)