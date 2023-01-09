package com.starters.hsge.presentation.main.user

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
        checkNickname()
        initListener()
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


            Timber.d("!!최초 닉 ${prefs.getString(OLD_NICKNAME, "")}")
            Timber.d("!!나중 닉 ${prefs.getString(NEW_NICKNAME, "")}")

        }
    }

    private fun setUserData() {
        if (!viewModel.nickNameFlag) {
            prefs.edit().putString(OLD_NICKNAME, viewModel.nickName).apply()
        } else {
            prefs.edit().putString(NEW_NICKNAME, viewModel.nickName).apply()
        }
        binding.tvEmail.text = viewModel.email
        binding.edtNickname.setText(viewModel.nickName)
        binding.ivUserProfile.setImageResource(viewModel.userIcon)

        Timber.d("!!플래그 ${viewModel.nickNameFlag}")
    }


    private fun initListener() {
        // 프로필 이미지 클릭
        binding.ivUserProfile.setOnClickListener {
            viewModel.nickNameFlag = true
            Timber.d("!!플래그2 ${viewModel.nickNameFlag}")
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
                    prefs.edit().remove(NEW_NICKNAME).apply()
                    viewModel.nickNameFlag = false
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
        //Timber.d("args ${args.userProfileInfo?.nickName}")
        //Timber.d("저장 ${prefs.getString(OLD_NICKNAME, "")}")
        if (prefs.getString(OLD_NICKNAME, "") == binding.edtNickname.text.toString()) {
            binding.edtNicknameLayout.error = null
            buttonFlag = true
            flagCheck()
        } else {
            val value = NicknameRequest(binding.edtNickname.text.toString())
            NicknameService(this).tryPostNickname(value)
            Timber.d("post 되냐?")
        }
    }

    private fun setTextWatcher() {
        binding.edtNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.nickName = binding.edtNickname.text.toString()
                //prefs.edit().putString(NEW_NICKNAME, p0.toString()).apply()
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
            Timber.d("닉네임? 성공")
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
        Timber.d("닉네임? $message")
        Log.d("UserNickname 오류", "오류: $message")
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

    companion object {
        const val OLD_NICKNAME = "oldNickName"
        const val NEW_NICKNAME = "newNickName"
    }
}