package com.starters.hsge.presentation.common.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.starters.hsge.data.model.remote.response.Message
import com.starters.hsge.presentation.common.util.DateFormat.changeDayFormat
import com.starters.hsge.presentation.common.util.DateFormat.changeMinFormat
import com.starters.hsge.presentation.main.chat.chatroom.ChatRoomViewModel
import java.text.SimpleDateFormat

@BindingAdapter("viewVisibility")
fun View.viewVisibility(isNeuter: Boolean) {
    when (isNeuter) {
        false -> {
            this.visibility = View.GONE
        }
        true -> {
        }
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("bindMessage", "bindMessageViewModel")
fun TextView.bindShouldMessageShowDividerText(message: Message?, viewModel: ChatRoomViewModel) {
    message?.let {
        val index = viewModel.messages.indexOf(message)

        if (index == 0) {
            this.visibility = View.GONE
        } else {
            val messageBefore = viewModel.messages[index - 1]

            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
            val beforeDate = dateFormat.parse(changeDayFormat(messageBefore.timeStamp))
            val afterDate = dateFormat.parse(changeDayFormat(message.timeStamp))

            val diff = (afterDate.time - beforeDate.time) / 1000
            val diffDay = diff / (60 * 60 * 24)

            if (diffDay > 0) {
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("bindSingleMessage", "bindSingleMessageViewModel")
fun TextView.bindShouldMessageShowTimeText(message: Message?, viewModel: ChatRoomViewModel) {
    message?.let {
        val index = viewModel.messages.indexOf(message)
        if (index == 0) {
            this.visibility = View.VISIBLE
        } else {
            val messageBefore = viewModel.messages[index - 1]

            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
            val beforeDate = dateFormat.parse(changeMinFormat(messageBefore.timeStamp))
            val afterDate = dateFormat.parse(changeMinFormat(message.timeStamp))

            val diff = (afterDate.time - beforeDate.time) / 1000
            val diffMin = diff / 60

            if (messageBefore.senderId != message.senderId) {
                this.visibility = View.VISIBLE
            } else {
                if (diffMin > 0) {
                    this.visibility = View.VISIBLE
                } else {
                    this.visibility = View.GONE
                }
            }
        }
    }
}