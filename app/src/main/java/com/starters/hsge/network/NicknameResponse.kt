package com.starters.hsge.network

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class NicknameResponse(
    @SerialName("data")val data: Boolean
)
