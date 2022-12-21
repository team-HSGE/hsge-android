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
import com.starters.hsge.databinding.FragmentChatReportOtherDialogBinding

class ChatReportOtherDialogFragment(private val okBtnClickListener: (String) -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentChatReportOtherDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chat_report_other_dialog,
            container,
            false
        )

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initListener()

    }

    private fun initListener() {
        binding.tvDialogOkBtn.setOnClickListener {
            okBtnClickListener.invoke(binding.editOtherReason.text.toString())
            dismiss()
        }

        binding.tvDialogCancelBtn.setOnClickListener {
            dismiss()
        }
    }

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

        dialogFragmentResize(requireContext(), this@ChatReportOtherDialogFragment, 0.9f, 0.30f)
    }

    companion object {
        const val TAG = "ReportOtherReasonDialog"
    }
}
