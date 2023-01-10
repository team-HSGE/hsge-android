package com.starters.hsge.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatListInfo(
    val roomId: Long,
    val nickname: String,
    val userIcon: Int,
    val message: String,
    val checked: Boolean,
    val active: Boolean,
    val firstDate: String
): Parcelable