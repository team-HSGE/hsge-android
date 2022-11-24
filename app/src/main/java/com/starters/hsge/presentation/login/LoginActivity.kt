package com.starters.hsge.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivityLoginBinding
import com.starters.hsge.network.ATResponse
import com.starters.hsge.network.AccessTokenInterface
import com.starters.hsge.network.RetrofitClient
import com.starters.hsge.presentation.common.base.BaseActivity
import com.starters.hsge.presentation.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private lateinit var callback: (OAuthToken?, Throwable?) -> Unit
    private var access_token : String = ""
    val TAG = "tagerror"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLoginInfo()
        callback()
        loginBtnClick()
    }

    fun checkLoginInfo() {
        //로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            } else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()

                //val intent = Intent(this, RegisterActivity::class.java)
                //startActivity(intent.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP)))
                //finish()
            }
        }
    }

    fun callback() {
        callback = { token, error ->
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
                Log.d("로그인", "로그인에 성공하였습니다!")
                access_token = token.accessToken
                Log.d("확인", "${access_token}")

                Log.d("access 토큰", "카카오톡으로 로그인 성공 ${token.accessToken}")
                Log.d("refresh 토큰", "카카오톡으로 로그인 성공 ${token.refreshToken}")

                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
    }

    fun loginBtnClick() {
        binding.tvLoginSignupBtn.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                //카카오톡이 있으면 카카오톡으로 로그인
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오 계정으로 로그인 시도 없이 로그인 최소로 처리(예 : 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                        tryPostAccessToken(access_token)
                    } else if (token != null) {
                        Log.d("access 토큰", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        Log.d("refresh 토큰", "카카오톡으로 로그인 성공 ${token.refreshToken}")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                tryPostAccessToken(access_token)
            }
        }
    }

    private fun tryPostAccessToken(access_token : String){
        val accessTokenInterface = RetrofitClient.sRetrofit.create(AccessTokenInterface::class.java)
        accessTokenInterface.postLogin(access_token).enqueue(object :
            Callback<ATResponse> {
            override fun onResponse(
                call: Call<ATResponse>,
                response: Response<ATResponse>
            ) {
                if(response.isSuccessful){
                    Log.d("성공", "${access_token}")
                }
            }

            override fun onFailure(call: Call<ATResponse>, t: Throwable) {
                Log.d("실패", t.message ?: "통신오류")
            }

        })
    }


}