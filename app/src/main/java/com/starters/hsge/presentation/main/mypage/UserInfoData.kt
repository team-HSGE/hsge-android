package com.starters.hsge.presentation.main.mypage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfoData(
    var profileImage: Int?,
    var nickname : String?
) : Parcelable
