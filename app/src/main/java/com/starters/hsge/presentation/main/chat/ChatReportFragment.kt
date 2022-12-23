package com.starters.hsge.presentation.main.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.ReportInterface
import com.starters.hsge.data.model.remote.request.ReportRequest
import com.starters.hsge.data.service.ReportService
import com.starters.hsge.databinding.FragmentChatReportBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BaseDialogFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.dialog.ChatReportOtherDialogFragment

class ChatReportFragment : BaseFragment<FragmentChatReportBinding>(R.layout.fragment_chat_report), ReportInterface {

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

        setNavigation()
        selectReason(reasonList)
        initListener()

    }

    private fun initListener(){
        binding.btnReport.setOnClickListener {
            // TODO : 피신고자 reportee에 넣기
            val reason = binding.tvChatReportSelectReason.text
            ReportService(this).tryPostReport(ReportRequest(reason.toString(), 10))

            findNavController().navigateUp()
        }
    }
    // 이유 선택
    private fun selectReason(reasonList: List<String>) {
        binding.tvChatReportSelectReason.setOnClickListener {
            reasonBottomSheet = BottomSheetDialog(reasonList)
            reasonBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
            reasonBottomSheet.setBottomSheetClickListener(object :
                BottomSheetDialog.BottomSheetClickListener {
                override fun onContentClick(content: String) {

                    if (content == "기타") {
                        val dialog = ChatReportOtherDialogFragment(
                            okBtnClickListener = {
                                if (it.isNotEmpty() && it.isNotBlank()) {
                                    binding.tvChatReportSelectReason.text = it
                                    isReason = true
                                    binding.btnReport.isEnabled = isReason
                                }
                            })
                        dialog.show(childFragmentManager, ChatReportOtherDialogFragment.TAG)
                    } else {
                        binding.tvChatReportSelectReason.text = content
                        isReason = true
                        binding.btnReport.isEnabled = isReason
                    }
                }
            })
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            val dialog = BaseDialogFragment("신고를 취소하시겠습니까?")

            dialog.setButtonClickListener(object : BaseDialogFragment.OnButtonClickListener {
                override fun onCancelBtnClicked() {

                }
                override fun onOkBtnClicked() {
                    findNavController().navigateUp()
                }
            })
            dialog.show(childFragmentManager, "CustomDialog")
        }
    }

    override fun onPostReportSuccess(isSuccess: Boolean, code: Int) {
        if(isSuccess){
            Log.d("Report", "성공")
        }else{
            Log.d("Report 오류", "Error code : ${code}")
        }    }

    override fun onPostReportFailure(message: String) {
        Log.d("Report 오류", "오류: $message")
    }
}