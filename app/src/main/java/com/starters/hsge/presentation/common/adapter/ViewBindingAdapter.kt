package com.starters.hsge.presentation.common.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.starters.hsge.data.model.remote.response.Message
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
fun TextView.bindShouldMessageShowTimeText(message: Message?, viewModel: ChatRoomViewModel) {
    message?.let {
        val messageBefore = viewModel.messages[viewModel.messages.size - 1]

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val beforeDate = dateFormat.parse(messageBefore.timeStamp)
        val afterDate = dateFormat.parse(message.timeStamp)

        val diff = (afterDate.time - beforeDate.time) / 1000
        val diffDay = diff / (60 * 60 * 24)

        if (diffDay > 0) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }
}