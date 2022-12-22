package com.starters.hsge.presentation.main.chatroom

import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentChatRoomBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import okhttp3.OkHttpClient
import okhttp3.Request

class ChatRoomFragment : BaseFragment<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {

    private lateinit var client: OkHttpClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        client = OkHttpClient()

        val request: Request = Request.Builder()
            .url("ws://192.168.0.59:8082/ws/chat")
            .build()
        val listener = WebSocketListener()

        client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()

    }
}