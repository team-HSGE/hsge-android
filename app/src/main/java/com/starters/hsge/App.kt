package com.starters.hsge

import android.app.Application
import android.content.SharedPreferences
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var prefs: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        KakaoSdk.init(this, "e9ee5f6eb7b2679f8f67b4938252e566")
        prefs = applicationContext.getSharedPreferences("HSGE", Application.MODE_PRIVATE)
    }
}