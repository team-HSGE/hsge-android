package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class NicknameResponse(
    @SerialName("data")val data: Boolean
)
