package com.starters.hsge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DogCard(
    val name: String,
    val imgUrl: String,
    val breed: String,
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