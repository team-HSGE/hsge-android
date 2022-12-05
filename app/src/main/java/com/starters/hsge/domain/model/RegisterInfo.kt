package com.starters.hsge.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterInfo(
    @SerialName("email") val email: String,
    @SerialName("nickname") val userNickName: String,
    @SerialName("profilePath") val userIcon: Int,
    @SerialName("petName") val dogName: String,
    @SerialName("age") val dogAge: String,
    @SerialName("gender") val dogSex: String,
    @SerialName("neutralization") val dogNeuter: Boolean = false,
    @SerialName("description") val description: String = "",
    @SerialName("picture") val picture: String = "",
    @SerialName("breed") val dogBreed: String,
    @SerialName("likeTag") val dogLikeTag: String,
    @SerialName("dislikeTag") val dogDislikeTag: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longtitude") val longitude: Double
)

