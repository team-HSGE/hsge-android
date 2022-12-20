package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class NicknameRequest(
    @SerialName("nickname") val nickname : String
)
