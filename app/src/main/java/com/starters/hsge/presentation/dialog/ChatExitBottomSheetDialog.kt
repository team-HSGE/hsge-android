package com.starters.hsge.presentation.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.ChatExitInterface
import com.starters.hsge.data.model.remote.request.ChatExitRequest
import com.starters.hsge.data.service.ChatExitService
import com.starters.hsge.databinding.FragmentChatExitBottomSheetDialogBinding
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.chatroom.ChatRoomFragment

class ChatExitBottomSheetDialog(private val roomId: Long, private val partnerId: Long): BottomSheetDialogFragment(), ChatExitInterface {

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
            // safeargs로 roomId전달하기
            findNavController().navigate(R.id.action_chatRoomFragment_to_chatReportFragment, bundleOf(
                "partnerId" to partnerId,
                "roomId" to roomId)
            )
            dismiss()
        }

        // 채팅 나가기
        binding.constChatExitExit.setOnClickListener {
            val exitDialog = BaseDialogFragment("채팅방을 나가시겠습니까?")

            exitDialog.setButtonClickListener(object : BaseDialogFragment.OnButtonClickListener {
                override fun onCancelBtnClicked() {

                }
                override fun onOkBtnClicked() {
                    //TODO : 채팅방 나가기 통신
                    ChatExitService(this@ChatExitBottomSheetDialog).tryPostChatExit(roomId, ChatExitRequest("DEFAULT", partnerId))
                    findNavController().navigateUp()
                    visibleBtmNav()
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
                    //TODO : 채팅방 나가기 통신
                    ChatExitService(this@ChatExitBottomSheetDialog).tryPostChatExit(roomId, ChatExitRequest("UNMATCH", partnerId))
                    //findNavController().navigateUp()

                    visibleBtmNav()
                }
            })
            unMatchDialog.show(childFragmentManager, "CustomDialog")
        }
    }

    companion object {
        const val TAG = "TagBottomSheetDialog"
    }

    override fun onPostChatExitSuccess(isSuccess: Boolean, code: Int) {
        if(isSuccess){
            //findNavController().navigateUp()
            Log.d("ChatExit_매칭 취소 / 나가기", "성공")
        }else{
            Log.d("ChatExit_매칭 취소 / 나가기 오류", "Error code : ${code}")
        }
    }

    override fun onPostChatExitFailure(message: String) {
        Log.d("ChatExit_매칭 취소 / 나가기 오류", "오류: $message")
    }

    private fun visibleBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }
}