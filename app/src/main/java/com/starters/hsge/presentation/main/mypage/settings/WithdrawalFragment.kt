package com.starters.hsge.presentation.main.mypage.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.user.UserApiClient
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.WithdrawalInterface
import com.starters.hsge.data.model.remote.request.FcmPostRequest
import com.starters.hsge.data.service.WithdrawalService
import com.starters.hsge.databinding.FragmentWithdrawalBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.WithdrawalDialogFragment
import com.starters.hsge.presentation.login.LoginActivity

class WithdrawalFragment: BaseFragment<FragmentWithdrawalBinding>(R.layout.fragment_withdrawal), WithdrawalInterface {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()

        binding.btnWithdrawal.setOnClickListener {
            val dialog = WithdrawalDialogFragment()

            dialog.setButtonClickListener(object: WithdrawalDialogFragment.OnButtonClickListener {
                override fun onCancelBtnClicked() {
                    // 취소 버튼 클릭했을 때 처리
                }

                override fun onWithdrawalBtnClicked() {
                    // 탈퇴 버튼 클릭했을 때 처리
                    UserApiClient.instance.unlink { error ->
                        if (error != null) {
                            Log.d("회원 탈퇴", "회원 탈퇴 실패 : $error")
                            showToast("다시 시도해주세요.")
                        }else {
                            WithdrawalService(this@WithdrawalFragment).tryDeleteUserInfo()

                        }
                    }
                }
            })

            dialog.show(childFragmentManager, "CustomDialog")
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun moveToLoginActivity() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        ActivityCompat.finishAffinity(requireActivity())
        startActivity(intent)
    }

    override fun onDeleteUserSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            // 회원 탈퇴 하기 (access Token sp에서 삭제 & DB에서 회원정보 제거)
            prefs.edit().clear().apply()
            moveToLoginActivity()
            showToast("회원탈퇴 되었습니다.")
            Log.d("회원탈퇴", "회원탈퇴 성공")
        }
    }

    override fun onDeleteUserFailure(message: String) {
        Log.d("User 삭제 오류", "오류: $message")
    }
}