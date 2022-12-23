package com.starters.hsge.presentation.common.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.starters.hsge.R

@BindingAdapter("bindProfileIcon")
fun ImageView.bindProfileIcon(resId: Int) {
    Glide.with(this)
        .load(resId)
        .into(this)
}

@BindingAdapter("bindDogPhoto")
fun ImageView.bindDogPhoto(imgUri: String) {
    Glide.with(this)
        .load(imgUri)
        .circleCrop()
        .into(this)
}

@BindingAdapter("bindDogSexIcon")
fun ImageView.bindSexIcon(sex: String) {
    when (sex) {
        "남" -> {
            Glide.with(this)
                .load(R.drawable.ic_gender_male_black)
                .into(this)
        }
        "여" -> {
            Glide.with(this)
                .load(R.drawable.ic_gender_female_black)
                .into(this)
        }
    }
}