package com.starters.hsge.presentation

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.starters.hsge.BuildConfig

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "${BuildConfig.NATIVE_APP_KEY}")
    }
}