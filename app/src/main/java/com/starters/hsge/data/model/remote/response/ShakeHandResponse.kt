package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ShakeHandResponse(
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("nickname") val nickname: String,
    @SerialName("userId") val userId: Int
)
