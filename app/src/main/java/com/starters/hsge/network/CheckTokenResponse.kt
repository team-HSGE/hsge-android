package com.starters.hsge.network

@kotlinx.serialization.Serializable
data class CheckTokenResponse(
    val message: String?,
    val refreshToken: String? = null,
    val accessToken: String? = null,
    val time : String? = null
)
