package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CurrentLocationPostRequest(
    @SerialName("lng") val lng: String,
    @SerialName("lat") val lat: String
)