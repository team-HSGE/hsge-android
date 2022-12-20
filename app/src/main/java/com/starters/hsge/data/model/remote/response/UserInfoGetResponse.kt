package com.starters.hsge.data.model.remote.response

@kotlinx.serialization.Serializable
data class UserInfoGetResponse(
    val latitude: Double,
    val longtitude: Double,
    val nickname: String,
    val profilePath: Int,
    val town: String,
    val radius: Double
)