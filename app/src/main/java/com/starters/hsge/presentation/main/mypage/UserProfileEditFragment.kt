package com.starters.hsge.presentation.main.mypage

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.NicknameInterface
import com.starters.hsge.data.interfaces.UserInfoPutInterface
import com.starters.hsge.data.model.remote.request.NicknameRequest
import com.starters.hsge.data.model.remote.request.UserInfoPutRequest
import com.starters.hsge.data.model.remote.response.NicknameResponse
import com.starters.hsge.data.service.NicknameService
import com.starters.hsge.data.service.UserInfoPutService
import com.starters.hsge.databinding.FragmentUserProfileEditBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity

class UserProfileEditFragment:BaseFragment<FragmentUserProfileEditBinding>(R.layout.fragment_user_profile_edit), UserInfoPutInterface, NicknameInterface {

    private lateinit var callback: OnBackPressedCallback

    private val args : UserProfileEditFragmentArgs by navArgs()
    private var nickNameFlag = false
    private var email : String? = ""
    private var profile : Int? = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = prefs.getString("email", "")

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
            profile = args.resId
            binding.ivUserProfile.setImageResource(profile!!)
            binding.edtNickname.setText(prefs.getString("nickname2", ""))

        } else {
            // 초기 세팅
            profile = args.userInfoData?.profileImage
            binding.ivUserProfile.setBackgroundResource(profile!!)
            binding.edtNickname.setText(args.userInfoData?.nickname ?: "")
            prefs.edit().putString("nickname", binding.edtNickname.text.toString()).apply()
            prefs.edit().putString("nickname2", binding.edtNickname.text.toString()).apply()
        }
    }

    private fun initListener() {
        // 프로필 이미지 클릭
        binding.ivUserProfile.setOnClickListener {
            val action = UserProfileEditFragmentDirections.actionUserProfileEditFragmentToUserProfileEditIconFragment(2)
            findNavController().navigate(action)
        }
        // 수정 버튼 클릭
        binding.btnEdit.setOnClickListener {
            val userInfo = UserInfoPutRequest(profilePath = profile!!, nickname = prefs.getString("nickname2", "테스트")!!)
            Log.d("확인", "${profile}, ${prefs.getString("nickname2", "")}")

            UserInfoPutService(this).tryPutUserInfo(userInfo)
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

            NicknameService(this).tryPostNickname(value)
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

    override fun onPostUserNicknameSuccess(nicknameResponse: NicknameResponse, isSuccess: Boolean, code: Int, nicknameRequest:NicknameRequest) {
        if (isSuccess) {
            val availability = nicknameResponse.data

            if (availability==false) {
                // 다시 입력하기
                binding.edtNicknameLayout.error = "이미 사용중인 닉네임입니다."
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

    override fun onPutUserInfoSuccess(isSuccess: Boolean, code: Int) {
        if (isSuccess) {
            Log.d("회원 정보 수정", "성공!")
            prefs.edit().remove("nickname").commit()
            prefs.edit().remove("nickname2").commit()

            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userProfileEditFragment_to_myPageFragment)

            showToast("수정이 완료되었습니다.")
            visibleBtmNav()
        }
    }

    override fun onPutUserInfoFailure(message: String) {
        Log.d("UserInfoPut 오류", "오류: $message")
    }
}