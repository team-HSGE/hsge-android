package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyDogResponse(
    @SerialName("age")
    val age: String,
    @SerialName("breed")
    val breed: String,
    @SerialName("description")
    val description: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Int,
    @SerialName("neutralization")
    val neutralization: Boolean,
    @SerialName("petName")
    val petName: String,
    @SerialName("picture")
    val picture: String,
    @SerialName("tag")
    val tag: MyDogTag
)

@Serializable
data class MyDogTag(
    @SerialName("tagDisLike")
    val tagDisLike: List<String>,
    @SerialName("tagLike")
    val tagLike: List<String>
)