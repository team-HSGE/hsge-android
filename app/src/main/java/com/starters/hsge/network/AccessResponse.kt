package com.starters.hsge.network

@kotlinx.serialization.Serializable
data class AccessResponse(
    val message: String,
    val data: String? = null
)
