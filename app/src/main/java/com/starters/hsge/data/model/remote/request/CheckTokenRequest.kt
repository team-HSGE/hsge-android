package com.starters.hsge.data.model.remote.request

@kotlinx.serialization.Serializable
data class CheckTokenRequest (
    val accessToken : String,
    val refreshToken : String
)