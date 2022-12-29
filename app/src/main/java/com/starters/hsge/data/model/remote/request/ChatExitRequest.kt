package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatExitRequest(

    @SerialName("type") val type: String,
    @SerialName("counterUserId") val counterUserId: Long
    )