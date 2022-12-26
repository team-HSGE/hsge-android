package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class DistanceRequest(
    @SerialName("radius") val radius: Double
)
