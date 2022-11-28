package com.starters.hsge.data.model

data class DogCard(
    val dogName: String,
    val imgUrl: String,
    val dogBreed: String,
    val dogAge: String,
    val isNeuter : Boolean,
    val tag: Tag,
)
data class Tag(
    val tagLike: List<String>,
    val tagDisLike: List<String>
)
