package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginResponse(
    val message: String,
    @SerialName("refreshToken") val refreshToken: String? = null,
    @SerialName("accessToken") val accessToken: String? = null
)
