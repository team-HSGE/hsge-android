package com.starters.hsge.network

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AccessRequest(
    @SerialName("accessToken") val accessToken : String
)
