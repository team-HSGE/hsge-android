package com.starters.hsge.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivityLoginBinding
import com.starters.hsge.data.model.remote.request.LoginRequest
import com.starters.hsge.data.model.remote.response.LoginResponse
import com.starters.hsge.data.interfaces.LoginInterface
import com.starters.hsge.data.service.LoginService
import com.starters.hsge.data.model.remote.request.FcmPostRequest
import com.starters.hsge.presentation.common.base.BaseActivity
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login), LoginInterface {

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

                val json = LoginRequest(access_token)
                LoginService(this).tryPostAccessToken(json)
                Log.d("json", "${json}")
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

            // FCM토큰 발급 및 sp에 저장
            fcmToken = task.result
            prefs.edit().putString("fcmToken", fcmToken).apply()
            Log.d("FCM토큰", "FCM토큰: ${fcmToken} / sp: ${prefs.getString("fcmToken", "")}")
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

    override fun onPostAccessTokenSuccess(loginResponse: LoginResponse?, isSuccess: Boolean, code: Int, error: String?) {
        if (isSuccess) {
            if (loginResponse?.message == "LOGIN") {
                Log.d("소셜로그인", "${loginResponse?.message}")

                // 로그인 성공 시, 발급받은 JWT + refresh JWT sp에 저장 (bearer 구분)
                prefs.edit().putString("BearerAccessToken", "Bearer ${loginResponse?.accessToken}").apply()
                prefs.edit().putString("BearerRefreshToken", "Bearer ${loginResponse?.refreshToken}").apply()
                prefs.edit().putString("NormalAccessToken", "${loginResponse?.accessToken}").apply()
                prefs.edit().putString("NormalRefreshToken", "${loginResponse?.refreshToken}").apply()

                Log.d("Bearer토큰", "access 토큰: ${loginResponse?.accessToken}\nrefresh 토큰: ${loginResponse?.refreshToken}")
                Log.d("Normal토큰", "access 토큰: ${loginResponse?.accessToken}\nrefresh 토큰: ${loginResponse?.refreshToken}")

                // FCM토큰 서버에 보내기
                val fcmToken = FcmPostRequest(fcmToken)
                LoginService(this).tryPostFcmToken(fcmToken)

                // 메인 화면 이동
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
                showToast("로그인에 성공하였습니다.")

            } else if (loginResponse?.message == "NEED_SIGNUP") {
                Log.d("소셜로그인", "${loginResponse?.message}")

                val intent = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        } else {
            val msg = error

            when(code) {
                400 -> {
                    when (msg) {
                        "REPORT LIMIT EXCEED" -> {
                            showToast("다중의 신고가 접수되어 앱 사용이 제한되었습니다. 관리자에게 문의하세요.")
                            Log.d("신고횟수", "신고 횟수가 7회 이상입니다. ")
                        }
                        "Access Token is not valid" -> {
                            // 카카오톡 accessToken 유효기간 끝났을 때
                            showToast("유효하지 않은 토큰값입니다.")
                        }
                    }
                }
            }
        }
    }

    override fun onPostAccessTokenFailure(message: String) {
        Log.d("Login 오류", "오류: $message")
    }

    override fun onPostFcmTokenSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            Log.d("FCM 토큰 보내기", "성공!")
        }
    }

    override fun onPostFcmTokenFailure(message: String) {
        Log.d("Fcm Token 보내기 오류", "오류: $message")
    }
}