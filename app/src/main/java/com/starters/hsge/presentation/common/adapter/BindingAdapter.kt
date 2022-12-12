package com.starters.hsge.presentation.common.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("bindProfileIcon")
fun ImageView.bindProfileIcon(resId: Int) {
    Glide.with(this)
        .load(resId)
        .into(this)
}