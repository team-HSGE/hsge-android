package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.NicknameInterface
import com.starters.hsge.data.model.remote.request.NicknameRequest
import com.starters.hsge.data.model.remote.response.NicknameResponse
import com.starters.hsge.data.service.NicknameService
import com.starters.hsge.databinding.FragmentUserNickNameBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserNickNameFragment :
    BaseFragment<FragmentUserNickNameBinding>(R.layout.fragment_user_nick_name), NicknameInterface {

    private var nickNameFlag = false
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTextWatcher()
        setNavigation()
        initListener()
        showUserNickName()
    }

    private fun setTextWatcher() {
        binding.edtUserNickName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("입력한 값", "${binding.edtUserNickName.text.toString()}")
                val value = NicknameRequest(binding.edtUserNickName.text.toString())
                NicknameService(this@UserNickNameFragment).tryPostNickname(value)
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
            findNavController().navigateUp()
        }
    }

    private fun flagCheck() {
        binding.btnNext.isEnabled = nickNameFlag
    }

    private fun verifyNickname(nickname: String): Boolean =
        nickname.matches(Regex("^[가-힣a-zA-Z0-9]{2,13}$"))

    private fun showUserNickName() {
        lifecycleScope.launch {
            if (registerViewModel.fetchUserNickname().first().isNotEmpty()) {
                binding.edtUserNickName.setText(registerViewModel.fetchUserNickname().first())
            }
        }
    }

    override fun onPostUserNicknameSuccess(
        nicknameResponse: NicknameResponse,
        isSuccess: Boolean,
        code: Int,
        nicknameRequest: NicknameRequest
    ) {
        if (isSuccess) {
            val availability = nicknameResponse.data
            Log.d("닉네임 설정 가능", "설정 가능 : $availability")

            if (availability == true) {
                // ds에 저장하기
                lifecycleScope.launch {
                    registerViewModel.saveUserNickname(nicknameRequest.nickname)
                }

            } else {
                // 다시 입력하기
                binding.tilUserNickName.error = "이미 사용중인 닉네임입니다."
                nickNameFlag = false
                flagCheck()
            }

        } else {
            Log.d("UserNickname 오류", "getNickname - onResponse : Error code ${code}")
        }
    }

    override fun onPostUserNicknameFailure(message: String) {
        Log.d("UserNickname 오류", "오류: $message")
    }
}
