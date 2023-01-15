package com.starters.hsge

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        Timber.plant(Timber.DebugTree())
        KakaoSdk.init(this, BuildConfig.APP_KAKAO_NATIVE_APP_KEY)
        prefs = applicationContext.getSharedPreferences("HSGE", MODE_PRIVATE)
    }
}