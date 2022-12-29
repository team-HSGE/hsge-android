package com.starters.hsge.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentChatExitBottomSheetDialogBinding

class ChatExitBottomSheetDialog: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChatExitBottomSheetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chat_exit_bottom_sheet_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListener()
    }


    private fun initListener() {
        // 신고
        binding.constChatExitReport.setOnClickListener {
            findNavController().navigate(R.id.action_chatRoomFragment_to_chatReportFragment)
            dismiss()
        }

        // 채팅 나가기
        binding.constChatExitExit.setOnClickListener {
            val exitDialog = BaseDialogFragment("채팅방을 나가시겠습니까?")

            exitDialog.setButtonClickListener(object : BaseDialogFragment.OnButtonClickListener {
                override fun onCancelBtnClicked() {

                }
                override fun onOkBtnClicked() {
                    findNavController().navigateUp()
                }
            })
            exitDialog.show(childFragmentManager, "CustomDialog")
        }

        // 매칭 취소
        binding.constChatExitUnmatch.setOnClickListener {
            val unMatchDialog = BaseDialogFragment("매칭을 취소하시겠습니까?")

            unMatchDialog.setButtonClickListener(object : BaseDialogFragment.OnButtonClickListener {
                override fun onCancelBtnClicked() {

                }
                override fun onOkBtnClicked() {
                    findNavController().navigateUp()
                }
            })
            unMatchDialog.show(childFragmentManager, "CustomDialog")
        }
    }

    companion object {
        const val TAG = "TagBottomSheetDialog"
    }
}