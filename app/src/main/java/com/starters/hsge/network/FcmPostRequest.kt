package com.starters.hsge.network

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class FcmPostRequest(
    @SerialName("token") val token : String
)
