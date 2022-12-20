package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserInfoPutRequest(
    @SerialName("profilePath") val profilePath : Int,
    @SerialName("nickname") val nickname : String
)
