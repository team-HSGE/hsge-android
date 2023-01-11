package com.starters.hsge.data.model.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserInfoResponse(
    val latitude: Double,
    val longtitude: Double,
    val nickname: String,
    val profilePath: Int,
    val town: String,
    val radius: Double
): Parcelable