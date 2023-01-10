package com.starters.hsge.presentation.main.mypage.user

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.starters.hsge.R
import com.starters.hsge.common.constants.iconToOrder
import com.starters.hsge.data.interfaces.NicknameInterface
import com.starters.hsge.data.model.remote.request.EditUserRequest
import com.starters.hsge.data.model.remote.request.NicknameRequest
import com.starters.hsge.data.model.remote.response.NicknameResponse
import com.starters.hsge.data.service.NicknameService
import com.starters.hsge.databinding.FragmentUserProfileEditBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.constants.OLD_NICKNAME
import com.starters.hsge.presentation.common.util.LoadingDialog
import com.starters.hsge.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserProfileEditFragment :
    BaseFragment<FragmentUserProfileEditBinding>(R.layout.fragment_user_profile_edit),
    NicknameInterface {

    private val viewModel: UserProfileEditViewModel by viewModels()

    private lateinit var callback: OnBackPressedCallback

    private val args: UserProfileEditFragmentArgs by navArgs()

    private var buttonFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initValue()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userProfile = args.userProfileInfo
        setUserData()
        initListener()
        checkNickname()
        setTextWatcher()
        setNavigation()
    }

    private fun initValue() {
        with(viewModel) {
            email = prefs.getString("email", "").toString()
            args.userProfileInfo?.let {
                nickName = it.nickName
                userIcon = it.userIcon
            }
        }
    }

    private fun setUserData() {
        binding.tvEmail.text = viewModel.email
        binding.tvEmail.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.edtNickname.setText(viewModel.nickName)
        binding.ivUserProfile.setImageResource(viewModel.userIcon)
    }


    private fun initListener() {
        // 프로필 이미지 클릭
        binding.ivUserProfile.setOnClickListener {
            val action =
                UserProfileEditFragmentDirections.actionUserProfileEditFragmentToProfileIconFragment2(
                    2,
                    viewModel.nickName
                )
            findNavController().navigate(action)
        }
        // 수정 버튼 클릭
        binding.btnEdit.setOnClickListener {
            viewModel.putUserInfo(
                EditUserRequest(
                    profilePath = viewModel.userIcon.iconToOrder(),
                    nickname = viewModel.nickName
                )
            )

            viewModel.mResponse.observe(viewLifecycleOwner) {
                if (it.isSuccessful) {
                    prefs.edit().remove(OLD_NICKNAME).apply()
                    LoadingDialog.dismissDogLoadingDialog()
                    showToast("수정이 완료되었습니다")
                    visibleBtmNav()
                    findNavController().navigateUp()
                } else {
                    LoadingDialog.dismissDogLoadingDialog()
                }
            }
        }
    }

    private fun checkNickname() {
        if (prefs.getString(OLD_NICKNAME, "") == binding.edtNickname.text.toString()) {
            binding.edtNicknameLayout.error = null
            buttonFlag = true
            flagCheck()
        } else {
            val value = NicknameRequest(binding.edtNickname.text.toString())
            NicknameService(this).tryPostNickname(value)

            /**
             * navigation action으로 이동하면
             * binding.edtNicknameLayout.error가 다시 null로 세팅 됨
             * 아래의 코드는 action으로 이동한 경우 재검사
             */
            if (binding.edtNickname.text!!.isBlank()) {
                binding.edtNicknameLayout.error = "닉네임을 입력해주세요."
                buttonFlag = false
            } else if (!verifyNickname(binding.edtNickname.text.toString())) {
                binding.edtNicknameLayout.error = "닉네임 양식이 맞지 않습니다."
                buttonFlag = false
            } else {
                buttonFlag = true
            }
            flagCheck()
        }
    }

    private fun setTextWatcher() {
        binding.edtNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Timber.d("!!setText 호출")
                viewModel.nickName = p0.toString()
                checkNickname()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    when {
                        p0.isBlank() -> {
                            binding.edtNicknameLayout.error = "닉네임을 입력해주세요."
                            buttonFlag = false
                        }
                        !verifyNickname(p0.toString()) -> {
                            binding.edtNicknameLayout.error = "닉네임 양식이 맞지 않습니다."
                            buttonFlag = false
                        }
                        else -> {
                            binding.edtNicknameLayout.error = null
                            buttonFlag = true
                        }
                    }
                    flagCheck()
                }
            }
        })
    }

    private fun flagCheck() {
        binding.btnEdit.isEnabled = buttonFlag
    }

    private fun verifyNickname(nickname: String): Boolean =
        nickname.matches(Regex("^[가-힣a-zA-Z0-9]{2,13}$"))

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            prefs.edit().remove(OLD_NICKNAME).apply()
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav() {
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }

    override fun onPostUserNicknameSuccess(
        nicknameResponse: NicknameResponse,
        isSuccess: Boolean,
        code: Int,
        nicknameRequest: NicknameRequest
    ) {
        if (isSuccess) {
            Timber.d("닉네임 성공")
            val availability = nicknameResponse.data
            if (!availability) {
                // 다시 입력하기
                binding.edtNicknameLayout.error = "이미 사용중인 닉네임입니다."
                buttonFlag = false
                flagCheck()
            }
        } else {
            Timber.d("닉네임? $code")
            Timber.tag("UserNickname 오류").d("getNickname - onResponse : Error code $code")
        }
    }

    override fun onPostUserNicknameFailure(message: String) {
        Timber.d("닉네임 오류 $message")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                prefs.edit().remove(OLD_NICKNAME).apply()
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
}