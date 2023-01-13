package com.starters.hsge.presentation.common.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object DateFormat{
    @SuppressLint("SimpleDateFormat")
    fun changeDayFormat(time: String): String {
        try {
            val oldFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
            val newFormat = SimpleDateFormat("yyyy.MM.dd 00:00:00")
            val oldDate = oldFormat.parse(time)
            return newFormat.format(oldDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    fun changeMinFormat(time: String): String {
        try {
            val oldFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
            val newFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:00")
            val oldDate = oldFormat.parse(time)
            return newFormat.format(oldDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}