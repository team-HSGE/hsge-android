package com.starters.hsge.network

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserInfoPutRequest(
    @SerialName("profilePath") val profilePath : Int,
    @SerialName("nickname") val nickname : String
)
