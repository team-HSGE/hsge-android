package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.Serializable
import com.google.gson.annotations.SerializedName

@Serializable
data class DogAgeResponse(
    @SerializedName("data")
    val data: List<Age>,
    @SerializedName("message")
    val message: String
)

@Serializable
data class Age(
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: String
)