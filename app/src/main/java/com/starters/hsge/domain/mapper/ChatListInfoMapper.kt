package com.starters.hsge.domain.mapper

import com.starters.hsge.common.constants.orderToIcon
import com.starters.hsge.data.model.remote.response.ChatListResponse
import com.starters.hsge.domain.model.ChatListInfo


fun ChatListResponse.mapToChatListInfo(): ChatListInfo =
    ChatListInfo(
        roomId = this.roomId,
        nickname = this.nickname,
        userIcon = this.userIcon.orderToIcon(),
        message = this.message,
        checked = this.checked,
        active = this.active,
        firstDate = this.firstDate
    )