package com.starters.hsge.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivityLoginBinding
import com.starters.hsge.data.model.remote.request.LoginRequest
import com.starters.hsge.data.model.remote.response.LoginResponse
import com.starters.hsge.data.api.LoginApi
import com.starters.hsge.network.RetrofitClient
import com.starters.hsge.presentation.common.base.BaseActivity
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var callback: (OAuthToken?, Throwable?) -> Unit
    private var access_token : String = ""
    private var fcmToken : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLoginInfo()
        getFcmToken()
        callback()
        loginBtnClick()
    }

    fun checkLoginInfo() {
        //로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.d("카카오톡 토큰 정보 보기", "실패")
            } else if (tokenInfo != null) {
                Log.d("카카오톡 토큰 정보 보기", "성공")

                // 회원가입 후에 다시 재접속 했을 경우 토큰 정보를 확인 -> 토큰이 있으면 다음 화면으로 바로 넘어감
//                val intent = Intent(this, RegisterActivity::class.java)
//                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                finish()
            }
        }
    }

    fun callback() {
        callback = { token, error ->
            //에러처리
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Log.d("에러", "접근이 거부 됨(동의 취소)")
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Log.d("에러", "유효하지 않는 앱")
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Log.d("에러", "인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Log.d("에러", "요청 파라미터 오류")
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Log.d("에러", "유효하지 않은 scope ID")
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Log.d("에러", "설정이 올바르지 않음(android key hash)")
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Log.d("에러", "서버 내부 에러")
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Log.d("에러", "앱이 요청 권한이 없음")
                    }
                    else -> { // Unknown
                        Log.d("에러", "기타 에러")
                    }
                }
            } else if (token != null) {
                //로그인 성공
                Log.d("로그인", "로그인에 성공하였습니다!")
                Log.d("카카오 access 토큰", "${token.accessToken}, ${token.accessTokenExpiresAt}")
                Log.d("카카오 refresh 토큰", "${token.refreshToken}, ${token.refreshTokenExpiresAt}")

                loadUserInfo()

                access_token = token.accessToken

                val json = LoginRequest(access_token) //API 수정 -> fcmToken 추가 예정
                Log.d("json", "${json}")
                Log.d("FCM토큰", "FCM토큰: ${fcmToken}")
                tryPostAccessToken(json)
            }
        }
    }

    fun loadUserInfo(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d("에러", "사용자 정보요청 실패")
            }
            else if (user != null) {
                val str= "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}"
                Log.d("회원정보", "${str}")

                // 카카오톡 계정 이메일 sp에 저장
                prefs.edit().putString("email", user.kakaoAccount?.email).apply()
            }
        }
    }

    fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("firebaseToken", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            fcmToken = task.result
        })
    }

    fun loginBtnClick() {
        binding.tvLoginSignupBtn.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                //카카오톡 앱 깔려있을 때
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                //카카오톡 앱 안깔려있을 때
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun tryPostAccessToken(accessToken : LoginRequest){
        val loginApi = RetrofitClient.sRetrofit.create(LoginApi::class.java)
        loginApi.postLogin(accessToken).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body() as LoginResponse

                    if (result.message == "LOGIN") {
                        Log.d("소셜로그인", "${result.message}")

                        // 로그인 성공 시, 발급받은 JWT + refresh JWT sp에 저장 (bearer 구분)
                        prefs.edit().putString("BearerAccessToken", "Bearer ${result.accessToken}").apply()
                        prefs.edit().putString("BearerRefreshToken", "Bearer ${result.refreshToken}").apply()
                        prefs.edit().putString("NormalAccessToken", "${result.accessToken}").apply()
                        prefs.edit().putString("NormalRefreshToken", "${result.refreshToken}").apply()

                        Log.d("Bearer토큰", "access 토큰: ${result.accessToken}\nrefresh 토큰: ${result.refreshToken}")
                        Log.d("Normal토큰", "access 토큰: ${result.accessToken}\nrefresh 토큰: ${result.refreshToken}")

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()

                        Toast.makeText(applicationContext, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                    } else if (result.message == "NEED_SIGNUP") {
                        Log.d("소셜로그인", "${result.message}")

                        val intent = Intent(applicationContext, RegisterActivity::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                } else  {
                    when(response.code()) {
                        400 -> {
                            // 카카오톡 accessToken 유효기간 끝났을 때
                            Toast.makeText(applicationContext, "유효하지 않은 토큰값입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("실패", t.message ?: "통신오류")

            }
        })
    }
}