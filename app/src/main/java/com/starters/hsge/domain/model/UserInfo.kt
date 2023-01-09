package com.starters.hsge.domain.model

data class UserInfo(
    val latitude: Double,
    val longtitude: Double,
    val nickname: String,
    val profilePath: Int,
    val town: String,
    val radius: Double
)