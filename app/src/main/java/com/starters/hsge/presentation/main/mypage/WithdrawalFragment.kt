package com.starters.hsge.presentation.main.mypage

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentWithdrawalBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.WithdrawalDialogFragment

class WithdrawalFragment: BaseFragment<FragmentWithdrawalBinding>(R.layout.fragment_withdrawal) {
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


}