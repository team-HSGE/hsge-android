package com.starters.hsge.presentation.main.mypage

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserProfileEditBinding
import com.starters.hsge.network.*
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileEditFragment:BaseFragment<FragmentUserProfileEditBinding>(R.layout.fragment_user_profile_edit) {

    private lateinit var callback: OnBackPressedCallback

    private val args : UserProfileEditFragmentArgs by navArgs()
    private var nickNameFlag = false
    private var email : String? = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = prefs.getString("email", "")
        binding.edtNicknameLayout.requestFocus()

        setUserData()
        initListener()
        checkNickname()
        setTextWatcher()
        setNavigation()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
                visibleBtmNav()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun setUserData() {
        binding.tvEmail.text = email
        binding.tvEmail.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        if (args.resId != 0) {
            // 프로필 이미지 클릭 이후 세팅
            binding.ivUserProfile.setImageResource(args.resId)
            binding.edtNickname.setText(prefs.getString("nickname2", ""))

        } else {
            // 초기 세팅
            binding.ivUserProfile.setImageResource(args.userInfoData?.profileImage ?: 2131165374)
            binding.edtNickname.setText(args.userInfoData?.nickname ?: "")
            prefs.edit().putString("nickname", binding.edtNickname.text.toString()).apply()
        }
    }

    private fun initListener() {
        // 프로필 이미지 클릭
        binding.ivUserProfile.setOnClickListener {
            val action = UserProfileEditFragmentDirections.actionUserProfileEditFragmentToUserProfileIconFragment2(2)
            findNavController().navigate(action)
        }
        // 수정 버튼 클릭
        binding.btnEdit.setOnClickListener {
        }
    }

    private fun checkNickname() {
        if (prefs.getString("nickname", "") == binding.edtNickname.text.toString()) {
            binding.edtNicknameLayout.error = null
            nickNameFlag = true
            flagCheck()
        } else {
            val value = NicknameRequest(binding.edtNickname.text.toString())
            Log.d("입력한 값", "${binding.edtNickname.text.toString()}")
            tryPostNickname(value)
        }
    }

    private fun setTextWatcher() {
        binding.edtNickname.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                prefs.edit().putString("nickname2", p0.toString()).apply()
                checkNickname()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    when {
                        p0.isNullOrBlank() -> {
                            binding.edtNicknameLayout.error = "닉네임을 입력해주세요."
                            nickNameFlag = false
                        }
                        !verifyNickname(p0.toString()) -> {
                            binding.edtNicknameLayout.error = "닉네임 양식이 맞지 않습니다."
                            nickNameFlag = false
                        }
                        else -> {
                            binding.edtNicknameLayout.error = null
                            nickNameFlag = true
                        }
                    }
                    flagCheck()
                }
            }
        })
    }

    private fun flagCheck() { binding.btnEdit.isEnabled = nickNameFlag }

    private fun  verifyNickname(nickname: String): Boolean = nickname.matches(Regex("^[가-힣a-zA-Z0-9]{2,13}$"))

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav(){ (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE }

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
                    val availability = result.data

                    if (availability==false) {
                        // 다시 입력하기
                        binding.edtNicknameLayout.error = "이미 사용중인 닉네임입니다."
                        nickNameFlag = false
                        flagCheck()
                    }
                } else {
                    Log.d("userNickname", "getNickname - onResponse : Error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                Log.d("userNickname", t.message ?: "통신오류")
            }
        })
    }


}