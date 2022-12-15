package com.starters.hsge.network

@kotlinx.serialization.Serializable
data class CheckTokenRequest (
    val accessToken : String,
    val refreshToken : String
)