package com.starters.hsge.network

@kotlinx.serialization.Serializable
data class CheckTokenResponse(
    val message: String,
    val refreshToken: String,
    val accessToken: String,
    val time : String
)
