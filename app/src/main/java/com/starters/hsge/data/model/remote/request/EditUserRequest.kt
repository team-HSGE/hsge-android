package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class EditUserRequest(
    @SerialName("profilePath")
    val profilePath : Int,
    @SerialName("nickname")
    val nickname : String
)
