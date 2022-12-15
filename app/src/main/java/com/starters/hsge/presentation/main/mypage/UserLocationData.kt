package com.starters.hsge.presentation.main.mypage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserLocationData(
    val town : String,
    val latitude : Double,
    val longitude : Double,
    val radius : Double
) : Parcelable
