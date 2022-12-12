package com.starters.hsge

import android.app.Application
import android.content.SharedPreferences
import com.kakao.sdk.common.KakaoSdk
import com.starters.hsge.presentation.common.base.BaseActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var prefs: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "e9ee5f6eb7b2679f8f67b4938252e566")

        prefs = applicationContext.getSharedPreferences("HSGE", Application.MODE_PRIVATE)
    }
}