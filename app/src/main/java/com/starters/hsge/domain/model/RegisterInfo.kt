package com.starters.hsge.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterInfo(
    val email: String,
    val userNickName: String,
    val userIcon: Int,
    val dogName: String,
    val dogAge: String,
    val dogSex: String,
    val dogNeuter: Boolean = false,
    val dogBreed: String,
    val dogLikeTag: String,
    val dogDislikeTag: String,
    val latitude: Double,
    val longitude: Double
)

