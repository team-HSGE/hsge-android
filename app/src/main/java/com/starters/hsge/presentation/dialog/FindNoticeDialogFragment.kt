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
import com.starters.hsge.databinding.FragmentDialogFindNoticeBinding

class FindNoticeDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogFindNoticeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_find_notice,
            container,
            false
        )

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(true)

        initDialog()

        return binding.root
    }

    fun initDialog() {
        binding.ivCloseBtn.setOnClickListener {
            buttonClickListener.onOkBtnClicked()
            dismiss()
        }
    }

    interface OnButtonClickListener {
        fun onOkBtnClicked()
    }

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener

}