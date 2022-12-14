package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val message: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)