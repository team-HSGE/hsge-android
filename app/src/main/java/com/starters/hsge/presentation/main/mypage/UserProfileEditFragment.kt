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
import com.starters.hsge.network.NicknameRequest
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.register.fragment.userLocation.UserLocationFragmentArgs

class UserProfileEditFragment:BaseFragment<FragmentUserProfileEditBinding>(R.layout.fragment_user_profile_edit) {

    private lateinit var callback: OnBackPressedCallback

    private val args : UserProfileEditFragmentArgs by navArgs()
    private var nickNameFlag = false
    private var email : String? = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("확인3", "${args.userInfoData?.profileImage}, ${args.userInfoData?.nickname}")
        email = prefs.getString("email", "")

        setUserData()
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
        binding.ivUserProfile.setImageResource(args.userInfoData!!.profileImage)
        binding.tvEmail.text = email
        binding.tvEmail.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.edtNickname.setText(args.userInfoData!!.nickname)
    }

    private fun setTextWatcher() {
        binding.edtNickname.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("입력한 값", "${binding.edtNickname.text.toString()}")
                val value = NicknameRequest(binding.edtNickname.text.toString())
                // API 연동
                // tryPostNickname(value)
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

    private fun flagCheck() {
        binding.btnEdit.isEnabled = nickNameFlag
    }

    private fun  verifyNickname(nickname: String): Boolean = nickname.matches(Regex("^[가-힣a-zA-Z0-9]{2,13}$"))

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }
}