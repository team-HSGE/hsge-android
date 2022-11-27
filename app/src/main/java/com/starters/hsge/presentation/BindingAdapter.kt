package com.starters.hsge.presentation

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("bindProfileIcon")
fun ImageView.bindProfileIcon(resId: Int) {
    Glide.with(this)
        .load(resId)
        .into(this)
}