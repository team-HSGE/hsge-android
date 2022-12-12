package com.starters.hsge.network

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class NicknameRequest(
    @SerialName("nickname") val nickname : String
)
