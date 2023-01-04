package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UsersLocationNearbyGetResponse(
    @SerialName("name") val name: String,
    @SerialName("lat") val lat: Double,
    @SerialName("lng") val lng: Double,
    @SerialName("userId") val userId: Long
)
