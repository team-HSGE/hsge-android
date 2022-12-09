package com.starters.hsge.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Data(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String
)

data class userLocation(
    val latitude: Double,
    val longtitude: Double,
    val town: String
)

data class Distance(
    val radius: Double
)
