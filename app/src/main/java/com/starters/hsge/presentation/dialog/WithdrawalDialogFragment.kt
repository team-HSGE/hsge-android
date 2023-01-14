package com.starters.hsge.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDialogWithdrawalBinding

class WithdrawalDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogWithdrawalBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_withdrawal,
            container,
            false
        )

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initDialog()

        return binding.root
    }

    fun initDialog() {
        binding.tvDialogCancelBtn.setOnClickListener {
            buttonClickListener.onCancelBtnClicked()
            dismiss()
        }

        binding.tvDialogWithdrawalBtn.setOnClickListener {
            buttonClickListener.onWithdrawalBtnClicked()
            dismiss()
        }
    }

    interface OnButtonClickListener {
        fun onCancelBtnClicked()
        fun onWithdrawalBtnClicked()
    }

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener

}