package com.starters.hsge.presentation.splash

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivityLoginBinding
import com.starters.hsge.presentation.common.base.BaseActivity
import com.starters.hsge.presentation.common.base.BaseActivity.Companion.prefs
import com.starters.hsge.presentation.login.LoginActivity
import com.starters.hsge.presentation.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_splash) {

    var isReady = false
    var accessToken : String = ""
    var refreshToken : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initSplashScreen()
    }

    private fun initSplashScreen() {
        initData()
        getSharedPreference()

        // 31 버전일 떄와 아닐 때의 분기 처리
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            val content : View = findViewById(android.R.id.content)
            // SplashScreen이 생성되고 그려질 때 계속해서 호출된다.
            content.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        // Check if the initial data is ready.
                        return if (isReady) {
                            // Splash Screen 제거
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            true
                        } else {
                            // content is not ready
                            false
                        }
                    }
                }
            )
            //startMainActivity()
            checkToken()
        } else {
            // 31 버전이 아닌 경우, 별도 처리.
            setContentView(R.layout.activity_splash)
            checkToken()
            //startMainActivity()
        }
    }

    private fun getSharedPreference() {
        accessToken = prefs.getString("accessToken", "")!!
        refreshToken = prefs.getString("refreshToken", "")!!
        Log.d("sp", "access: ${accessToken}\nrefresh: ${refreshToken}")
    }

    private fun initData() {
        // 별도의 데이터 처리가 없기 때문에 3초의 딜레이를 줌.
        // 선행되어야 하는 작업이 있는 경우, 이곳에서 처리 후 isReady를 변경.
        CoroutineScope(Dispatchers.IO).launch {
            delay(DURATION)
            isReady = true
        }
    }

    private fun checkToken() {
        if (!prefs.contains("accessToken")){
            startLoginActivity()
        } else {
            startMainActivity()
        }
    }

    private fun startLoginActivity() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(DURATION)
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    private fun startMainActivity() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(DURATION)
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val DURATION : Long = 1000
    }

}

