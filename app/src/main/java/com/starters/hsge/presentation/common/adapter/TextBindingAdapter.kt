package com.starters.hsge.presentation.common.adapter

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.starters.hsge.presentation.common.util.DateFormat.changeDayFormat
import java.text.SimpleDateFormat
import java.util.*

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

@SuppressLint("SetTextI18n")
@BindingAdapter("dogBreedFormat")
fun TextView.dogBreedFormat(breed: String) {
    if (breed == "나만의 유일한 믹스") {
        this.text = "나만의\n유일한 믹스"
    } else if (breed.length > 5) {
        val newFormatBreed = breed.replace(" ", "\n")
        this.text = newFormatBreed
    }
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

@BindingAdapter("singleMessageTimeFormat")
fun TextView.singleMessageTimeFormat(date: String) {
    val format = date.split(" ")[1].substring(0, 5)
    this.text = format
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("lastDateFormat")
fun TextView.lastDateFormat(lastTime: String) {
    val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
    val now = dateFormat.format(Date(System.currentTimeMillis()))

    val nowDate = dateFormat.parse(changeDayFormat(now))
    val lastMessageDate = dateFormat.parse(changeDayFormat(lastTime))

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
        diffDay == 0L -> this.text = time
        diffDay > 0 ->
            if (diffDay < 2L) {
                this.text = "어제"
            } else {
                this.text = fullDate
            }
    }
}

@BindingAdapter("messageDivider")
fun TextView.messageDivider(date: String) {
    val dividerDate = date.substring(0, 11)
    this.text = dividerDate
}