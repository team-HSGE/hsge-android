package com.starters.hsge.domain.mapper

import com.starters.hsge.common.constants.orderToIcon
import com.starters.hsge.data.model.remote.response.UserInfoResponse
import com.starters.hsge.domain.model.UserInfo

fun UserInfoResponse.mapToUserInfo(): UserInfo =
    UserInfo(
        latitude = this.latitude,
        longtitude = this.longtitude,
        nickname = this.nickname,
        profilePath = this.profilePath.orderToIcon(),
        town = this.town,
        radius = this.radius
    )
