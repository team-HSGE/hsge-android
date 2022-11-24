package com.starters.hsge.presentation

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.starters.hsge.BuildConfig

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "e9ee5f6eb7b2679f8f67b4938252e566")
    }
}