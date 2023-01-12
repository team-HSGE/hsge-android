package com.starters.hsge.presentation.common.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("timeFormat")
fun timeFormat(textView: TextView, date: String) {
    val format = date.split(" ")[1].substring(0, 5)
    textView.text = format
}

@BindingAdapter("neuterFormat")
fun neuterFormat(textView: TextView, isNeuter: Boolean) {
    when (isNeuter) {
        false -> {
            textView.text = ""
        }
        true -> {
            textView.text = "중성화"
        }
    }
}

@BindingAdapter("firstDateFormat")
fun TextView.firstDateFormat(date: String) {
    val oldFormatList = date.split(".").toList()
    val newFormat = oldFormatList[0] + "년 " + oldFormatList[1] + "월 " + oldFormatList[2] + "일"
    this.text = newFormat
}

@BindingAdapter("dogBreedFormat")
fun TextView.dogBreedFormat(breed: String) {
    var newFormatBreed = breed
    if (breed.length > 5) {
        newFormatBreed = breed.replace(" ", "\n")
    }
    this.text = newFormatBreed
}

@BindingAdapter("townFormat")
fun TextView.townFormat(town: String?) {
    town?.let {
        val oldFormatList = town.split(" ")
        var newFormat = ""
        if (oldFormatList.size == 3) {
            newFormat = oldFormatList[1] + " " + oldFormatList[2]
            this.text = newFormat
        } else if (oldFormatList.size > 3) {
            newFormat = oldFormatList[1] + " " + oldFormatList[2] + " " + oldFormatList[3]
            this.text = newFormat
        }
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("lastDateFormat")
fun TextView.lastDateFormat(lastTime: String) {
    val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
    val now = dateFormat.format(Date(System.currentTimeMillis()))

    val nowDate = dateFormat.parse(now)
    val lastMessageDate = dateFormat.parse(lastTime)

    val diff = (nowDate.time - lastMessageDate.time) / 1000

    val fullDate = lastTime.split(" ")[0]
    val date = lastTime.substring(5, 10)
    val time = lastTime.split(" ")[1].substring(0, 5)

    val diffSec = diff
    val diffMin = diff / 60
    val diffHour = diff / (60 * 60)
    val diffDay = diff / (60 * 60 * 24)
    val diffMonth = diff / (60 * 60 * 24 * 30)
    val diffYears = diff / (60 * 60 * 24 * 30 * 12)

    when {
        diffMonth > 0 -> this.text = fullDate
        diffDay > 0 ->
            if (diffDay < 2L) {
                this.text = "어제"
            } else {
                this.text = date
            }
        diffSec > 0 -> this.text = time
    }
}
