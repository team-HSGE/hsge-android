package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class IsLikeRequest(
    @SerialName("like") val like: Boolean
)
