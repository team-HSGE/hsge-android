package com.starters.hsge.presentation.main.mypage.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.user.UserApiClient
import com.starters.hsge.R
import com.starters.hsge.data.api.WithdrawalApi
import com.starters.hsge.data.interfaces.SettingsInterface
import com.starters.hsge.data.interfaces.WithdrawalInterface
import com.starters.hsge.data.service.SettingsService
import com.starters.hsge.data.service.WithdrawalService
import com.starters.hsge.databinding.FragmentWithdrawalBinding
import com.starters.hsge.network.RetrofitClient
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.WithdrawalDialogFragment
import com.starters.hsge.presentation.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                            Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
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
            Log.d("회원탈퇴", "회원탈퇴 성공")

            // access token & fcm token 날리기
            WithdrawalService(this).tryDeleteFcmToken()
        }
    }

    override fun onDeleteUserFailure(message: String) {
        Log.d("User 삭제 오류", "오류: $message")
    }

    override fun onDeleteFcmTokenSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            Log.d("FCM토큰 삭제", "성공!")
            prefs.edit().clear().apply()

            moveToLoginActivity()
            Toast.makeText(context, "회원탈퇴 되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDeleteFcmTokenFailure(message: String) {
        Log.d("FCM토큰 삭제 오류", "오류: $message")
    }
}