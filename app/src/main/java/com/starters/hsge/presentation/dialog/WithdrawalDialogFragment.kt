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

        initDialog()

        return binding.root
    }

    fun initDialog(){
        binding.tvDialogCancelBtn.setOnClickListener {
            buttonClickListener. onCancelBtnClicked()
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


    override fun onResume() {
        super.onResume()

        fun dialogFragmentResize(context: Context, dialogFragment: DialogFragment, width: Float, height: Float) {

            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            if (Build.VERSION.SDK_INT < 30) {

                val display = windowManager.defaultDisplay
                val size = Point()

                display.getSize(size)

                val window = dialogFragment.dialog?.window

                val x = (size.x * width).toInt()
                val y = (size.y * height).toInt()
                window?.setLayout(x, y)

            } else {

                val rect = windowManager.currentWindowMetrics.bounds

                val window = dialogFragment.dialog?.window

                val x = (rect.width() * width).toInt()
                val y = (rect.height() * height).toInt()

                window?.setLayout(x, y)
            }
        }

        dialogFragmentResize(requireContext(), this@WithdrawalDialogFragment, 0.8f, 0.22f)
    }
}