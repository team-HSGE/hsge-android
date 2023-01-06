package com.starters.hsge.domain.mapper

import com.starters.hsge.data.model.remote.response.ChatListResponse
import com.starters.hsge.data.model.remote.response.UserInfoResponse
import com.starters.hsge.domain.model.ChatListInfo
import com.starters.hsge.domain.model.UserInfo
import com.starters.hsge.presentation.common.util.UserIconFormat.applyIntToUserIcon

fun UserInfoResponse.mapToUserInfo(): UserInfo =
    UserInfo(
        latitude = this.latitude,
        longtitude = this.longtitude,
        nickname = this.nickname,
        profilePath = applyIntToUserIcon(this.profilePath),
        town = this.town,
        radius = this.radius
    )

fun ChatListResponse.mapToChatListInfo(): ChatListInfo =
    ChatListInfo(
        roomId = this.roomId,
        nickname = this.nickname,
        userIcon = applyIntToUserIcon(this.userIcon),
        message = this.message,
        checked = this.checked,
        active = this.active,
        firstDate = this.firstDate
    )