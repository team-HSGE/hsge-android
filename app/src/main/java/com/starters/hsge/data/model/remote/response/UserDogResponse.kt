package com.starters.hsge.data.model.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserDogResponse(
    @SerialName("age")
    val age: String? = null,
    @SerialName("breed")
    val breed: String? = null,
    @SerialName("description")
    val description: String?,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Int,
    @SerialName("neutralization")
    val neutralization: Boolean,
    @SerialName("petName")
    val petName: String,
    @SerialName("petImg")
    val picture: List<String>,
    @SerialName("tag")
    val tag: UserDogTag,
    @SerialName("ageDto")
    val ageDto: AgeDto,
    @SerialName("breedDto")
    val breedDto: BreedDto
): Parcelable

@Serializable
@Parcelize
data class UserDogTag(
    @SerialName("tagDisLike")
    val tagDisLike: List<String>,
    @SerialName("tagLike")
    val tagLike: List<String>
): Parcelable

@Serializable
@Parcelize
data class AgeDto(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String
): Parcelable

@Serializable
@Parcelize
data class BreedDto(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String
): Parcelable