package com.starters.hsge.presentation.main.chatroom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.R
import com.starters.hsge.data.model.remote.response.Message
import com.starters.hsge.databinding.FragmentChatRoomBinding
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.dialog.ChatExitBottomSheetDialog
import com.starters.hsge.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import timber.log.Timber
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    lateinit var binding: FragmentChatRoomBinding

    private val args: ChatRoomFragmentArgs by navArgs()

    private lateinit var chatExitBottomSheetDialog: ChatExitBottomSheetDialog
    private lateinit var callback: OnBackPressedCallback
    private lateinit var listAdapterObserver: RecyclerView.AdapterDataObserver
    private lateinit var adapter: MessageListAdapter
    private var isOpen = false

    private val chatRoomViewModel: ChatRoomViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_chat_room, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatInfo = args.chatInfo

        val chatInfo = args.chatInfo
        chatRoomViewModel.roomId = chatInfo.roomId
        chatRoomViewModel.active = chatInfo.active
        chatRoomViewModel.partnerNickName = chatInfo.nickname

        setChatView()
        setupView()
        initListener()
        setNavigation()
        setupListAdapter()
        setupToolbar()

        val url = "wss://dev.hsge.site/ws/websocket"
        //val url = "ws://192.168.0.8:8081/ws/websocket" // 채팅 테스트 서버
        val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        // stomp 연결
        stompClient.connect()

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Timber.i("OPEN!!")

                    // subscribe 채널 구독, 내가 보내는 메세지와 상대가 보내는 메세지를 구독할 수 있음
                    stompClient.topic("/sub/chat/room/${chatRoomViewModel.roomId}")
                        .subscribe { topicMessage ->
                            // 메세지 디코딩
                            val messageFromJson =
                                Json.decodeFromString<Message>(topicMessage.payload)
                            Timber.d("메세지 구독 $messageFromJson")

                            // 리사이클러뷰에 데이터 추가
                            lifecycleScope.launch(Dispatchers.Main) {
                                chatRoomViewModel.chatList.value?.let {
                                    it.messageList.add(messageFromJson)
                                    Timber.d("메세지 리스트 ${it.messageList}")
                                    adapter.notifyDataSetChanged()
                                    binding.rvMessages.smoothScrollToPosition(it.messageList.size - 1)
                                }
                            }
                        }

                    //메세지 보내기
                    binding.btnSend.setOnClickListener {
                        if (stompClient.isConnected) {
                            if (binding.edtMessage.text.isNotEmpty() && binding.edtMessage.text.isNotBlank()) {
                                val data = JSONObject()
                                data.put("senderId", chatRoomViewModel.myId)
                                data.put("message", binding.edtMessage.text)
                                data.put("roomId", chatRoomViewModel.roomId)

                                stompClient.send("/pub/chat/message", data.toString()).subscribe()
                                binding.edtMessage.text.clear()
                            }
                        }
                    }
                }
                LifecycleEvent.Type.CLOSED -> {
                    Timber.i("CLOSED!!")

                }
                LifecycleEvent.Type.ERROR -> {
                    Timber.tag("ERROR!!").e(lifecycleEvent.exception)
                }
                else -> {
                    Timber.i("ELSE!!")
                }
            }
        }
    }

    private fun setChatView() {
        when (chatRoomViewModel.active) {
            false -> {
                binding.toolBar.menu.findItem(R.id.menu_report).isVisible = false
            }
            true -> {
                binding.ivPartnerProfileLarge.visibility = View.GONE
                binding.tvPartnerNickname.visibility = View.GONE
                binding.tvChatroomExplanation.visibility = View.GONE
                binding.tvChatroomTime.visibility = View.GONE

            }
        }
    }

    private fun setupListAdapter() {
        // adapter의 데이터 변화 감지
        listAdapterObserver = (object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                chatRoomViewModel.messages.forEach {
                    if (it.senderId == chatRoomViewModel.myId) {
                        // active 변경
                        lifecycleScope.launch(Dispatchers.IO) {
                            chatRoomViewModel.postChatRoomState(chatRoomViewModel.roomId)
                        }
                        // inactive 상태의 뷰 지우기
                        binding.ivPartnerProfileLarge.visibility = View.GONE
                        binding.tvPartnerNickname.visibility = View.GONE
                        binding.tvChatroomExplanation.visibility = View.GONE
                        binding.tvChatroomTime.visibility = View.GONE
                        binding.toolBar.menu.findItem(R.id.menu_report).isVisible = true
                    }
                }
            }
        })

        // 리사이클러뷰 스크롤 위치 셋팅
        binding.rvMessages.apply {
            //키보드 올라올 때 레이아웃 이동
            addOnLayoutChangeListener(onLayoutChangeListener)

            viewTreeObserver.addOnScrollChangedListener {
                if (isScrollable() && !isOpen) {
                    setStackFromEnd()
                    removeOnLayoutChangeListener(onLayoutChangeListener)
                }
            }
        }


        // 채팅 대화 목록 호출
        chatRoomViewModel.getMessageInfo(chatRoomViewModel.roomId).observe(viewLifecycleOwner) {
            it?.let {
                binding.messageInfo = it
                chatRoomViewModel.messages = it.messageList
                chatRoomViewModel.partnerId = it.userInfo.otherUserId
                chatRoomViewModel.myId = it.userInfo.userId
                adapter = MessageListAdapter(it.userInfo.userId)
                adapter.registerAdapterDataObserver(listAdapterObserver)
                binding.rvMessages.adapter = adapter
                adapter.submitList(it.messageList)
            }
        }
    }

    private fun setupToolbar() {
        // 파트너 아이콘 클릭
        binding.ivToolbarPartnerIcon.setOnClickListener {
            findNavController().navigate(
                R.id.action_chatRoomFragment_to_partnerDogsFragment,
                bundleOf(
                    PARTNER_ID to chatRoomViewModel.partnerId,
                    PARTNER_NICKNAME to chatRoomViewModel.partnerNickName
                )
            )
        }

        // 메뉴 클릭
        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_report -> {
                    chatExitBottomSheetDialog = ChatExitBottomSheetDialog(
                        chatRoomViewModel.roomId,
                        chatRoomViewModel.partnerId
                    )
                    chatExitBottomSheetDialog.show(childFragmentManager, BottomSheetDialog.TAG)
                    true
                }
                else -> false
            }
        }
    }

    private fun initListener() {
        // 파트너 아이콘 click
        binding.ivPartnerProfileLarge.setOnClickListener {
            findNavController().navigate(
                R.id.action_chatRoomFragment_to_partnerDogsFragment,
                bundleOf(
                    PARTNER_ID to chatRoomViewModel.partnerId,
                    PARTNER_NICKNAME to chatRoomViewModel.partnerNickName
                )
            )
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

    private fun visibleBtmNav() {
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }

    private fun setupView() {
        // 키보드 Open/Close 체크
        binding.layoutContainer.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.layoutContainer.getWindowVisibleDisplayFrame(rect)

            val rootViewHeight = binding.layoutContainer.rootView.height
            val heightDiff = rootViewHeight - rect.height()
            isOpen = heightDiff > rootViewHeight * 0.25
        }
    }

    private val onLayoutChangeListener =
        View.OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            // 키보드 올라오면서 높이 변화
            if (bottom < oldBottom) {
                binding.rvMessages.scrollBy(0, oldBottom - bottom)
            }
        }

    companion object {
        const val PARTNER_ID = "partnerId"
        const val PARTNER_NICKNAME = "partnerNickName"
    }
}