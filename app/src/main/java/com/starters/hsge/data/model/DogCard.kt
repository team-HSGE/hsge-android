package com.starters.hsge.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DogCard(

    @SerialName("name") val Dogname: String,
    val imgUrl: String,
    val DogBreed: String,
    val age: String,
    val sex: String,
    val isNeuter : Boolean,
    val tag: Tag
    //serialName
)

@Serializable
data class Tag(
    val tagLike: List<String>,
    val tagDisLike: List<String>
)