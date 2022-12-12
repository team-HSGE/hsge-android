package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class DogCard(
    val petId: Int,
    val name: String,
    val picture: String,
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

