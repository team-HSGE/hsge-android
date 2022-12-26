package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatListResponse(
    @SerialName("nickname") val nickname: String,
    @SerialName("profilePath") val userIcon: Int,
    @SerialName("latestMessage") val message: String,
    @SerialName("checked") val checked: Boolean, // 메세지 확인 여부
    @SerialName("active") val active: Boolean, // 대화 시작된 채팅방(하단 목록)


    // val date: Int
)