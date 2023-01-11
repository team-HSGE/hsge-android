package com.starters.hsge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationInfo(
    val latitude: Double,
    val longitude: Double,
    val town: String,
    val radius: Double
): Parcelable