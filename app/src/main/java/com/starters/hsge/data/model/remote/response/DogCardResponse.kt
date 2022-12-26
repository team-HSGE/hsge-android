package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DogCard(
    @SerialName("petId") val petId: Int,
    @SerialName("name") val name: String,
    @SerialName("petImg") val petImg: List<String>,
    @SerialName("breed") val breed: String,
    @SerialName("age") val age: String,
    @SerialName("sex") val sex: String,
    @SerialName("isNeuter") val isNeuter : Boolean,
    @SerialName("tag") val tag: Tag
    //serialName
)

@Serializable
data class Tag(
    @SerialName("tagLike") val tagLike: List<String>,
    @SerialName("tagDisLike") val tagDisLike: List<String>
)

