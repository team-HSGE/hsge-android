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