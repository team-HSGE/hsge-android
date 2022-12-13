package com.starters.hsge.data.model.remote.request

data class SignUpResponse(
    val accessToken: String,
    val refreshToken: String
)