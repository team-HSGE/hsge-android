package com.starters.hsge.network

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AccessResponse(
    val message: String,
    @SerialName("refreshToken") val refreshToken: String? = null,
    @SerialName("accessToken") val accessToken: String? = null
)
