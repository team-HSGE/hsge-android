package com.starters.hsge.presentation.common.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("timeFormat")
fun timeFormat(textView: TextView, date: String) {
    val format = date.split(" ")[1].substring(0, 5)
    textView.text = format
}

@BindingAdapter("neuterFormat")
fun neuterFormat(textView: TextView, isNeuter: Boolean) {
    when(isNeuter) {
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
    val oldFormatList = date.split("-").toList()
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