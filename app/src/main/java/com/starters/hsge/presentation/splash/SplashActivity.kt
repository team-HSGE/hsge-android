package com.starters.hsge.presentation.splash

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.starters.hsge.R
import com.starters.hsge.data.api.CheckTokenApi
import com.starters.hsge.data.model.remote.request.CheckTokenRequest
import com.starters.hsge.data.model.remote.response.CheckTokenResponse
import com.starters.hsge.databinding.ActivitySplashBinding
import com.starters.hsge.network.*
import com.starters.hsge.presentation.common.base.BaseActivity
import com.starters.hsge.presentation.dialog.SplashDialogFragment
import com.starters.hsge.presentation.login.LoginActivity
import com.starters.hsge.presentation.main.MainActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    var isReady = false
    var accessToken: String = ""
    var refreshToken: String = ""

    companion object {
        private const val DURATION : Long = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        initSplashScreen()
        createNotificationChannel(this)
    }

    //알림 권한
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "my-notification-channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = "my notification channel description"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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
            setContentView(R.layout.activity_splash)
            checkNetwork()
        }
    }

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
        accessToken = prefs.getString("NormalAccessToken", "")!!
        refreshToken = prefs.getString("NormalRefreshToken", "")!!
        Log.d("sp", "access: ${accessToken}\nrefresh: ${refreshToken}")
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

    // Token 값 확인
    private fun checkToken() {
        if (prefs.contains("NormalAccessToken")){
            val check = CheckTokenRequest(accessToken = accessToken, refreshToken = refreshToken)
            tryPostCheckToken(check)

        } else {
            startLoginActivity()
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

    private fun tryPostCheckToken(checkToken: CheckTokenRequest){
        val checkTokenApi = RetrofitClient.sRetrofit.create(CheckTokenApi::class.java)
        checkTokenApi.postToken(checkToken).enqueue(object :
            Callback<CheckTokenResponse?> {
            override fun onResponse(
                call: Call<CheckTokenResponse?>,
                response: Response<CheckTokenResponse?>
            ) {
                val result = response.body() as CheckTokenResponse?

                if (response.isSuccessful) {
                    // sp에 access랑 refresh 저장 (갱신)
                    prefs.edit().putString("NormalAccessToken", "${result?.accessToken}").apply()
                    prefs.edit().putString("NormalRefreshToken", "${result?.refreshToken}").apply()
                    prefs.edit().putString("BearerAccessToken", "Bearer ${result?.accessToken}").apply()
                    prefs.edit().putString("BearerRefreshToken", "Bearer ${result?.refreshToken}").apply()
                    Log.d("토큰 갱신", "메시지: ${result?.message}\naccess토큰: ${result?.accessToken}\nrefresh토큰: ${result?.refreshToken}")

                    startMainActivity()

                } else  {
                    val msg = result?.message

                    when(response.code()) {
                        401 -> {
                            when (msg) {
                                "NO_ACCESS" -> {
                                    Log.d("토큰 상태", "access, refresh 토큰이 없습니다.")
                                }
                                "TOKEN type Bearer" -> {
                                    Log.d("토큰 상태", "접두사 Bearer가 없습니다.")
                                }
                                "NEED_NEW_LOGIN" -> {
                                    Toast.makeText(applicationContext, "세션이 만료되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
                                    Log.d("토큰 상태", "Refresh 토큰이 만료되었습니다. / ${result.time}")
                                }
                            }
                        }
                        403 -> {
                            when (msg) {
                                "Malformed Token" -> {
                                    Log.d("토큰 상태", "토큰이 조작되었습니다.")
                                }
                                "BadSignatured Token" -> {
                                    Log.d("토큰 상태", "토큰이 형식에 맞지 않습니다.")
                                }
                            }
                        }
                    }

                    startLoginActivity()
                }
            }

            override fun onFailure(call: Call<CheckTokenResponse?>, t: Throwable) {
                Log.d("실패", t.message ?: "통신오류")

            }
        })
    }

}


