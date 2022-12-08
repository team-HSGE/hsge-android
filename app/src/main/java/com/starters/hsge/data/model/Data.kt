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