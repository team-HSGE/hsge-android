package com.starters.hsge.domain.model

import com.starters.hsge.common.constants.UserIcon

data class UserInfo(
    val latitude: Double,
    val longtitude: Double,
    val nickname: String,
    val profilePath: UserIcon,
    val town: String,
    val radius: Double
)