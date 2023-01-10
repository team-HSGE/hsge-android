package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class FcmPostRequest(
    @SerialName("token") val token : String?
)
