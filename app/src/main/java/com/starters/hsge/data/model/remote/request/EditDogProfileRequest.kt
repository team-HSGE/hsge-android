package com.starters.hsge.data.model.remote.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class EditDogProfileRequest(
    @SerialName("age")
    val age: String?,
    @SerialName("breed")
    val breed: String,
    @SerialName("description")
    val description: String?,
    @SerialName("dislikeTag")
    val dislikeTag: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("likeTag")
    val likeTag: String,
    @SerialName("neutralization")
    val neutralization: Boolean,
    @SerialName("petName")
    val petName: String,
)