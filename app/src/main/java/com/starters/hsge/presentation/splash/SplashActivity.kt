package com.starters.hsge.presentation.splash

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivitySplashBinding
import com.starters.hsge.presentation.common.base.BaseActivity
import com.starters.hsge.presentation.dialog.SplashDialogFragment
import com.starters.hsge.presentation.login.LoginActivity
import com.starters.hsge.presentation.main.MainActivity
import kotlinx.coroutines.*

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    var isReady = false
    var accessToken : String = ""
    var refreshToken : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initSplashScreen()
    }

    // 스플래시 초기화
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
            checkNetwork()
        } else {
            // 31 버전이 아닌 경우, 별도 처리.
            checkNetwork()
        }
    }

    // 데이터 처리
    private fun initData() {
        // 별도의 데이터 처리가 없기 때문에 3초의 딜레이를 줌.
        // 선행되어야 하는 작업이 있는 경우, 이곳에서 처리 후 isReady를 변경.
        CoroutineScope(Dispatchers.IO).launch {
            delay(DURATION)
            isReady = true
        }
    }

    // sp 불러오기
    private fun getSharedPreference() {
        accessToken = prefs.getString("accessToken", "")!!
        refreshToken = prefs.getString("refreshToken", "")!!
        Log.d("sp", "access: ${accessToken}\nrefresh: ${refreshToken}")
    }

    // Token 값 확인
    private fun checkToken() {
        if (!prefs.contains("accessToken")){
            isReady = true
            startLoginActivity()
        } else {
            isReady = true
            startMainActivity()
        }
    }

    // 네트워크 dialog
    private fun setNetworkDialog() {
        val dialog = SplashDialogFragment()
        dialog.setButtonClickListener(object: SplashDialogFragment.OnButtonClickListener {
            override fun onOkBtnClicked() {
                checkNetwork()
            }
        })
        dialog.show(supportFragmentManager, "CustomDialog")
    }

    // 네트워크 상태 체크 (T/F)
    private fun isConnected(): Boolean {
        var connected = false
        try {
            val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected

        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message!!)
        }
        return connected
    }

    // 네트워크 상태 체크에 따른 처리
    private fun checkNetwork() {
        if (isConnected()) {
            Log.d("네트워크", "성공!")
                checkToken()
        } else {
            Log.d("네트워크", "실패!")
                setNetworkDialog()
        }
    }

    // 로그인 화면 이동
    private fun startLoginActivity() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(DURATION)
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    // 메인 화면 이동
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

