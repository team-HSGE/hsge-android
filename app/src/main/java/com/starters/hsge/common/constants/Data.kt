package com.starters.hsge.common.constants

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Data(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String
)