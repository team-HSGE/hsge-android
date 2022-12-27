package com.starters.hsge.presentation.main.chatroom

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentChatRoomBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.dialog.ChatExitBottomSheetDialog
import com.starters.hsge.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import ua.naiksoftware.stomp.Stomp

@AndroidEntryPoint
class ChatRoomFragment : BaseFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private lateinit var chatExitBottomSheetDialog: ChatExitBottomSheetDialog
    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * @author
         * initListener()를 타고 가시면 툴바 버튼 클릭 시 action을 정의할 수 있습니다
         */

        initListener()
        setNavigation()

        Log.d("tesst", "chatRoom")
        val url = "ws://[domain]/connect/websocket"
        val intervalMillis = 1000L
        val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

//        fun runStomp() {
//            stompClient.topic("/topic/~").subscribe { topicMessage ->
//                Log.i("message Receive", topicMessage.payload)
//            }
//
//
//            // 헤더를 넣고 싶으면 connect할 때 설정한 헤더들을 넣어주면 됨
//            val headerList = arrayListOf<StompHeader>()
//            headerList.add(StompHeader("inviteCode","test0912"))
//            headerList.add(StompHeader("username", text.value))
//            headerList.add(StompHeader("positionType", "1"))
//            stompClient.connect(headerList)
//
//
//            //stopmClient의 lifecycle 변경에 따라 로그를 찍는 코드
//            stompClient.lifecycle().subscribe { lifecycleEvent ->
//                when (lifecycleEvent.type) {
//                    LifecycleEvent.Type.OPENED -> {
//                        Log.i("OPEND", "!!")
//                    }
//                    LifecycleEvent.Type.CLOSED -> {
//                        Log.i("CLOSED", "!!")
//
//                    }
//                    LifecycleEvent.Type.ERROR -> {
//                        Log.i("ERROR", "!!")
//                        Log.e("CONNECT ERROR", lifecycleEvent.exception.toString())
//                    }
//                    else ->{
//                        Log.i("ELSE", lifecycleEvent.message)
//                    }
//                }
//            }
//
//            // 채팅을 보낼 때 채팅 메세지의 값에 맞춰서 보낼 주소에 넣어주기기
//            val data =JSONObject()
//            data.put("userKey", text.value)
//            data.put("positionType", "1")
//            data.put("content", "test")
//            data.put("messageType", "CHAT")
//            data.put("destRoomCode", "test0912")
//
//            //정책에 맞게 문자 전송
//            stompClient.send("/stream/chat/send", data.toString()).subscribe()
//
//        }
    }

    private fun initListener() {
        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_report -> {
                    chatExitBottomSheetDialog = ChatExitBottomSheetDialog()
                    chatExitBottomSheetDialog.show(childFragmentManager, BottomSheetDialog.TAG)
                    true
                }
                else -> false
            }
        }

        // 파트너 아이콘 click
        binding.ivPartnerProfileLarge.setOnClickListener {
            findNavController().navigate(R.id.action_chatRoomFragment_to_partnerDogsFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
                visibleBtmNav()
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
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }
}