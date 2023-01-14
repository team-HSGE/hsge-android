package com.starters.hsge.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDialogConfirmBinding

class ConfirmDialogFragment(private val text: String) : DialogFragment() {

    private lateinit var binding: FragmentDialogConfirmBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_confirm,
            container,
            false
        )

        // 레이아웃 배경 투명하게 바꾸기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        initDialog()
        setDialogTitle()
        return binding.root
    }

    fun setDialogTitle() {
        binding.tvDialogTitle.text = text
    }

    fun initDialog() {
        binding.tvDialogCancelBtn.setOnClickListener {
            buttonClickListener.onCancelBtnClicked()
            dismiss()
        }

        binding.tvDialogConfirmBtn.setOnClickListener {
            buttonClickListener.onOkBtnClicked()
            dismiss()
        }
    }

    interface OnButtonClickListener {
        fun onCancelBtnClicked()
        fun onOkBtnClicked()
    }

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
}