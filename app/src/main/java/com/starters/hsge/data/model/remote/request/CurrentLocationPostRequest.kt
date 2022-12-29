package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CurrentLocationPostRequest(
    @SerialName("latitude") val latitude: String,
    @SerialName("longitude") val longitude: String
)