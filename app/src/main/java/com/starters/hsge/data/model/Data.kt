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

data class Location(
    val latitude: Double,
    val longitude: Double
)

data class Distance(
    val distance: Double
)
