package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ChatExitRequest(
    @Serializable
    @SerialName("type") val type: String,
    @SerialName("counterUserId") val counterUserId: Long
    )