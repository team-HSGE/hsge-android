package com.starters.hsge.presentation.main.chat.report

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.ChatExitInterface
import com.starters.hsge.data.interfaces.ReportInterface
import com.starters.hsge.data.model.remote.request.ChatExitRequest
import com.starters.hsge.data.model.remote.request.ReportRequest
import com.starters.hsge.data.service.ChatExitService
import com.starters.hsge.data.service.ReportService
import com.starters.hsge.databinding.FragmentChatReportBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.LoadingDialog
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.dialog.ChatReportOtherDialogFragment
import com.starters.hsge.presentation.dialog.ConfirmDialogFragment
import com.starters.hsge.presentation.main.MainActivity

class ChatReportFragment : BaseFragment<FragmentChatReportBinding>(R.layout.fragment_chat_report), ReportInterface, ChatExitInterface {

    private lateinit var reasonBottomSheet: BottomSheetDialog
    private lateinit var callback: OnBackPressedCallback
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

        val partnerId = requireArguments().getLong("partnerId")
        val roomId = requireArguments().getLong("roomId")
        Log.d("방 정보", "$roomId $partnerId")
    }

    private fun initListener(){
        binding.btnReport.setOnClickListener {
            val partnerId = requireArguments().getLong("partnerId")
            val roomId = requireArguments().getLong("roomId")
            val reason = binding.tvChatReportSelectReason.text
            ReportService(this).tryPostReport(ReportRequest(reason.toString(), partnerId))
            ChatExitService(this).tryPostChatExit(roomId, ChatExitRequest("REPORT", partnerId))
            LoadingDialog.showDogLoadingDialog(requireContext())
            visibleBtmNav()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showCancelDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            showCancelDialog()
        }
    }

    private fun showCancelDialog() {
        val dialog = ConfirmDialogFragment("신고를 취소하시겠습니까?")
        dialog.setButtonClickListener(object : ConfirmDialogFragment.OnButtonClickListener {
            override fun onCancelBtnClicked() {
            }
            override fun onOkBtnClicked() {
                findNavController().navigateUp()
            }
        })
        dialog.show(childFragmentManager, "CustomDialog")
    }

    override fun onPostReportSuccess(isSuccess: Boolean, code: Int) {
        if(isSuccess){
            Log.d("Report", "성공")
            LoadingDialog.dismissDogLoadingDialog()
        }else{
            Log.d("Report 오류", "Error code : ${code}")
            LoadingDialog.dismissDogLoadingDialog()
            showToast("잠시 후 다시 시도해주세요")
        }
    }

    override fun onPostReportFailure(message: String) {
        Log.d("Report 오류", "오류: $message")
        LoadingDialog.dismissDogLoadingDialog()
        showToast("잠시 후 다시 시도해주세요")
    }

    override fun onPostChatExitSuccess(isSuccess: Boolean, code: Int) {
        if(isSuccess){
            showToast("신고가 완료되었습니다.")
            LoadingDialog.dismissDogLoadingDialog()
            findNavController().navigate(R.id.action_chatReportFragment_to_chatFragment)
            Log.d("ChatExit_신고", "성공")
        }else{
            Log.d("ChatExit_신고 오류", "Error code : ${code}")
            LoadingDialog.dismissDogLoadingDialog()
            showToast("잠시 후 다시 시도해주세요")
        }
    }

    override fun onPostChatExitFailure(message: String) {
        Log.d("ChatExit_신고 오류", "오류: $message")
        LoadingDialog.dismissDogLoadingDialog()
        showToast("잠시 후 다시 시도해주세요")
    }

    private fun visibleBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }
}