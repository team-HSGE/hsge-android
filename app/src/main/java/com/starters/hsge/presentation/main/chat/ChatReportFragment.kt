package com.starters.hsge.presentation.main.chat

import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentChatReportBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.dialog.ChatReportOtherDialogFragment

class ChatReportFragment : BaseFragment<FragmentChatReportBinding>(R.layout.fragment_chat_report) {

    private lateinit var reasonBottomSheet: BottomSheetDialog
    private var isReason = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reasonList = listOf<String>(
            "직접적인 신체적 피해 발생",
            "폭력적이거나 위협적인 행위",
            "영업 활동을 함",
            "나체 또는 성적인 콘텐츠",
            "기타"
        )

        selectReason(reasonList)
    }

    private fun selectReason(reasonList: List<String>) {
        binding.tvChatReportSelectReason.setOnClickListener {
            reasonBottomSheet = BottomSheetDialog(reasonList)
            reasonBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
            reasonBottomSheet.setBottomSheetClickListener(object :
                BottomSheetDialog.BottomSheetClickListener {
                override fun onContentClick(content: String) {

                    if(content == "기타") {
                        val dialog = ChatReportOtherDialogFragment(
                            okBtnClickListener = {
                            if(it.isNotEmpty() && it.isNotBlank()){
                                binding.tvChatReportSelectReason.text = it
                                isReason = true
                                binding.btnReport.isEnabled = isReason
                            }
                        })
                        dialog.show(childFragmentManager, ChatReportOtherDialogFragment.TAG)
                    } else{
                        binding.tvChatReportSelectReason.text = content
                        isReason = true
                        binding.btnReport.isEnabled = isReason
                    }
                }
            })
        }
    }
}