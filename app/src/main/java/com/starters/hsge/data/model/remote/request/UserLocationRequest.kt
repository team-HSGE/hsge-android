package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLocationRequest(
    @SerialName("latitude") val latitude: Double,
    @SerialName("longtitude") val longtitude: Double,
    @SerialName("town") val town: String
)
