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
import com.starters.hsge.databinding.FragmentDialogChatReportOtherBinding

class ChatReportOtherDialogFragment(private val okBtnClickListener: (String) -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentDialogChatReportOtherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_chat_report_other,
            container,
            false
        )

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initListener()
    }

    private fun initListener() {
        binding.tvDialogOkBtn.setOnClickListener {
            okBtnClickListener.invoke(binding.edtOtherReason.text.toString())
            dismiss()
        }

        binding.tvDialogCancelBtn.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "ReportOtherReasonDialog"
    }
}
