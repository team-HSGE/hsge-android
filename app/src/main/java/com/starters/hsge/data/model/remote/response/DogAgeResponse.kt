package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DogAgeResponse(
    @SerialName("data")
    val data: List<Age>,
    @SerialName("message")
    val message: String
)

@Serializable
data class Age(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String
)