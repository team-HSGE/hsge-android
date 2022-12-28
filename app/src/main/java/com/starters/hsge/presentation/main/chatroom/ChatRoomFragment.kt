package com.starters.hsge.presentation.main.chatroom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.R
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
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    lateinit var binding : FragmentChatRoomBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_room, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setChatView()
        setupView()
        initListener()
        setNavigation()
        setListAdapter()
        setToolbar()

        val url = "ws://192.168.0.8:8081/ws/websocket"
        val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        // stomp 연결
        stompClient.connect()

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.i("OPEND", "!!")

                    // subscribe 채널 구독, 내가 보내는 메세지와 상대가 보내는 메세지를 구독할 수 있음
                    stompClient.topic("/sub/chat/room/1").subscribe { topicMessage ->
                        // 메세지 디코딩
                        val messageFromJson = Json.decodeFromString<Message>(topicMessage.payload)

                        // 리사이클러뷰에 데이터 추가
                        lifecycleScope.launch(Dispatchers.Main) {
                            chatRoomViewModel.chatList.value?.let {
                                it.messageList.add(messageFromJson)
                                Log.d("메세지 리스트 데이터", "${it.messageList}")
                                adapter.notifyDataSetChanged()
                                binding.rvMessages.smoothScrollToPosition(it.messageList.size - 1)
                            }
                        }
                    }

                    //메세지 보내기
                    binding.btnSend.setOnClickListener {
                        //TODO: 데이터 넣기
                        if (binding.edtMessage.text.isNotEmpty() && binding.edtMessage.text.isNotBlank()) {
                            val data = JSONObject()
                            data.put("senderId", 1)
                            data.put("message", binding.edtMessage.text)
                            data.put("roomId", 1)

                            stompClient.send("/pub/chat/message", data.toString()).subscribe()
                            binding.edtMessage.text.clear()
                        }
                    }
                }
                LifecycleEvent.Type.CLOSED -> {
                    //Log.i("CLOSED", "!!")

                }
                LifecycleEvent.Type.ERROR -> {
                    //Log.i("ERROR", "!!")
                    //Log.e("CONNECT ERROR", lifecycleEvent.exception.toString())
                }
                else -> {
                    Log.i("ELSE", lifecycleEvent.message)
                }
            }
        }
    }

    private fun setChatView() {
        when (requireArguments().getInt("active")) {
            0 -> {
                //TODO: 첫 번째 메세지를 보낼 때 visible 처리
            }
            1 -> {
                binding.ivPartnerProfileLarge.visibility = View.GONE
                binding.tvLikeWho.visibility = View.GONE
                binding.tvChatroomExplanation.visibility = View.GONE
                binding.tvChatroomTime.visibility = View.GONE
            }
        }
    }

    private fun setListAdapter() {
//        listAdapterObserver = (object : RecyclerView.AdapterDataObserver() {
//            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                binding.rvMessages.scrollToPosition(positionStart)
//            }
//        })

        binding.rvMessages.apply {
            //키보드 올라올 때 레이아웃 이동
            addOnLayoutChangeListener(onLayoutChangeListener)

            //
            viewTreeObserver.addOnScrollChangedListener {
                if (isScrollable() && !isOpen) {
                    setStackFromEnd()
                    removeOnLayoutChangeListener(onLayoutChangeListener)
                }
            }
        }

        chatRoomViewModel.getChatInfo(1).observe(viewLifecycleOwner) {
            adapter = MessageListAdapter(it.userInfo.userId)
            //adapter.registerAdapterDataObserver(listAdapterObserver)
            binding.rvMessages.adapter = adapter
            adapter.submitList(it.messageList)
        }
    }

    private fun setToolbar() {
        chatRoomViewModel.getChatInfo(1).observe(viewLifecycleOwner) {
            binding.toolBar.title = it.userInfo.nickname
        }
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
        View.OnLayoutChangeListener {  _, _, _, _, bottom, _, _, _, oldBottom ->
            // 키보드 올라오면서 높이 변화
            if (bottom < oldBottom) {
                binding.rvMessages.scrollBy(0, oldBottom - bottom)
            }
        }
}