package com.starters.hsge.presentation.register.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserNickNameBinding
import com.starters.hsge.network.*
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.login.LoginActivity
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class UserNickNameFragment : BaseFragment<FragmentUserNickNameBinding>(R.layout.fragment_user_nick_name) {

    private var nickNameFlag = false
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTextWatcher()
        setNavigation()
        initListener()
    }

    private fun setTextWatcher() {
        binding.edtUserNickName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("입력한 값", "${binding.edtUserNickName.text.toString()}")
                val value = NicknameRequest(binding.edtUserNickName.text.toString())
                tryPostNickname(value)
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    when {
                        p0.isNullOrBlank() -> {
                            binding.tilUserNickName.error = "닉네임을 입력해주세요."
                            nickNameFlag = false
                        }
                        !verifyNickname(p0.toString()) -> {
                            binding.tilUserNickName.error = "닉네임 양식이 맞지 않습니다."
                            nickNameFlag = false
                        }
                        else -> {
                            binding.tilUserNickName.error = null
                            nickNameFlag = true
                        }
                    }
                    flagCheck()
                }
            }
        })
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userNickNameFragment_to_userImageFragment)
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            finishAffinity(requireActivity())
            startActivity(intent)
        }
    }

    private fun tryPostNickname(value: NicknameRequest) {
        val nicknameInterface = RetrofitClient.sRetrofit.create(NicknameInterface::class.java)
        nicknameInterface.postNickname(nickname = value).enqueue(object :
            Callback<NicknameResponse> {
            override fun onResponse(
                call: Call<NicknameResponse>,
                response: Response<NicknameResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body() as NicknameResponse

                    // true면 사용가능, false면 중복
                    val availability = result.data
                    Log.d("닉네임 설정 가능", "$availability / $value")

                    if (availability==true) {
                        // ds에 저장하기
                        lifecycleScope.launch {
                            registerViewModel.saveUserNickname(value.nickname)
                        }

                    } else {
                        // 다시 입력하기
                        binding.tilUserNickName.error = "이미 사용중인 닉네임입니다."
                        nickNameFlag = false
                        flagCheck()
                    }

                } else {
                    Log.d(
                        "userNickname", "getNickname - onResponse : Error code ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                Log.d("userNickname", t.message ?: "통신오류")
            }

        })
    }

    private fun flagCheck() {
        binding.btnNext.isEnabled = nickNameFlag
    }

    private fun  verifyNickname(nickname: String): Boolean = nickname.matches(Regex("^[가-힣a-zA-Z0-9]{2,13}$"))
}
