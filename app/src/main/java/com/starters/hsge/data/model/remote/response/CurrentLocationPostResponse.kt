package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CurrentLocationPostResponse(
    @SerialName("name") val name: String
)
