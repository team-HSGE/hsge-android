package com.starters.hsge.data.model.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class MyDogResponse(
    @SerialName("age")
    val age: String,
    @SerialName("breed")
    val breed: String,
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
    val tag: MyDogTag
): Parcelable

@Serializable
@Parcelize
data class MyDogTag(
    @SerialName("tagDisLike")
    val tagDisLike: List<String>,
    @SerialName("tagLike")
    val tagLike: List<String>
): Parcelable