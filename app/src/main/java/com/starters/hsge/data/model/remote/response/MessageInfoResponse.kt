package com.starters.hsge.data.model.remote.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class MessageInfoResponse(
    @SerialName("userInfo")
    val userInfo: UserInfo,
    @SerialName("messageList")
    val messageList: MutableList<Message>
)

@Serializable
data class Message(
    @SerialName("senderId")
    val senderId: Long,
    @SerialName("message")
    val message: String,
    @SerialName("createdDate")
    val timeStamp: String,
    @SerialName("roomId")
    val roomId: Long? = null,
)

@Serializable
data class UserInfo(
    @SerialName("userId")
    val userId: Long,
    @SerialName("otherUserId")
    val otherUserId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("profilePath")
    val profilePath: Int,
)