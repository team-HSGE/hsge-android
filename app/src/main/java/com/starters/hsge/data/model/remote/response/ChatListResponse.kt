package com.starters.hsge.data.model.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ChatListResponse(
    @SerialName("roomId") val roomId: Long,
    @SerialName("nickname") val nickname: String,
    @SerialName("profilePath") val userIcon: Int,
    @SerialName("latestMessage") val message: String,
    @SerialName("checked") val checked: Boolean, // 메세지 확인 여부
    @SerialName("active") val active: Boolean, // 대화 시작된 채팅방(하단 목록)
    @SerialName("firstDate") val firstDate: String,
    @SerialName("lastDate") val lastDate: String?
): Parcelable