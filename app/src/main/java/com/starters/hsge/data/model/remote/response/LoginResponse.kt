package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginResponse(
    @SerialName("message") var message: String? = "",
    @SerialName("refreshToken") var refreshToken: String? = "",
    @SerialName("accessToken") var accessToken: String? = ""
)
