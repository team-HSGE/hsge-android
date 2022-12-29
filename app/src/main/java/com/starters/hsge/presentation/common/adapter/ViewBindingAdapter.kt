package com.starters.hsge.presentation.common.adapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("viewVisibility")
fun View.viewVisibility(isNeuter: Boolean) {
    when(isNeuter) {
        false -> {
            this.visibility = View.GONE
        }
        true -> {

        }
    }
}