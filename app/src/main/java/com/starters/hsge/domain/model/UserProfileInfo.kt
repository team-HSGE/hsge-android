package com.starters.hsge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileInfo(
    val nickName: String,
    val userIcon: Int
): Parcelable