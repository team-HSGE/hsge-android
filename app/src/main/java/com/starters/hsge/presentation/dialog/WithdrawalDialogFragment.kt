package com.starters.hsge.presentation.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDialogWithdrawalBinding

class WithdrawalDialogFragment: DialogFragment() {

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
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

//        initDialog()

        return binding.root
    }

//    fun initDialog(){
//        binding.tvDialogCancelBtn.setOnClickListener {
//            buttonClickListener. onCancelBtnClicked()
//            dismiss()
//        }
//
//        binding.tvDialogWithdrawalBtn.setOnClickListener {
//            buttonClickListener.onWithdrawalBtnClicked()
//            dismiss()
//        }
//    }
//
//    override fun onStart() {
//        super.onStart();
//        val lp : WindowManager.LayoutParams  =  WindowManager.LayoutParams()
//        lp.copyFrom(dialog!!.window!!.attributes)
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        val window: Window = dialog!!.window!!
//        window.attributes =lp
//    }
//
//    interface OnButtonClickListener {
//        fun onCancelBtnClicked()
//        fun onWithdrawalBtnClicked()
//    }
//
//    // 클릭 이벤트 설정
//    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
//        this.buttonClickListener = buttonClickListener
//    }
//    // 클릭 이벤트 실행
//    private lateinit var buttonClickListener: OnButtonClickListener


}