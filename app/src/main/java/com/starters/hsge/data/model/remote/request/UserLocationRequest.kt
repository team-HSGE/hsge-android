package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class UserLocationRequest(
    val latitude: Double,
    val longtitude: Double,
    val town: String
)
