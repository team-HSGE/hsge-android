package com.starters.hsge.network

@kotlinx.serialization.Serializable
data class AccessResponse(
    val accessToken: String,
    val refreshToken: String
)
