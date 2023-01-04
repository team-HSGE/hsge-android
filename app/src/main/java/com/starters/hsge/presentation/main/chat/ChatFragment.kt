package com.starters.hsge.presentation.main.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.chatListInterface
import com.starters.hsge.data.model.remote.response.ChatListResponse
import com.starters.hsge.data.service.ChatListService
import com.starters.hsge.databinding.FragmentChatBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.LoadingLottieDialog
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.chat.adapter.ChatListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat), chatListInterface {

    private lateinit var chatListAdapter: ChatListAdapter

    private var likedPeopleList = mutableListOf<ChatListResponse>()
    private var chatList = mutableListOf<ChatListResponse>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.d("성공", "onResume")
        likedPeopleList.clear()
        chatList.clear()
        ChatListService(this).tryGetChatList()
        LoadingLottieDialog.showDogLoadingDialog(requireContext())
    }

    private fun goneBtmNav() {
        (activity as MainActivity).binding.navigationMain.visibility = View.GONE
    }

    override fun onGetChatListSuccess(
        chatListResponse: List<ChatListResponse?>?,
        isSuccess: Boolean,
        code: Int
    ) {
        if (isSuccess) {
            for (element in chatListResponse!!) {
                if (element!!.active) { // active
                    chatList.add(element)
                } else { // inactive
                    likedPeopleList.add(element)
                }
            }

            // inactive_좋아요
            chatListAdapter = ChatListAdapter(likedPeopleList, itemClickListener = {
                goneBtmNav()
            })

            binding.chatRvLikedPeople.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.chatRvLikedPeople.adapter = chatListAdapter
            Log.d("likeList", "성공, $likedPeopleList")

            // active_채팅
            chatListAdapter = ChatListAdapter(chatList, itemClickListener = {
                goneBtmNav()
            })

            binding.chatRvChatList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.chatRvChatList.adapter = chatListAdapter
            Log.d("ChatList", "성공, $chatList")
            Log.d("ChatList_all", "성공, $chatListResponse")
            LoadingLottieDialog.dismissDogLoadingDialog()
        } else {
            Log.d("ChatList 오류", "Error code : ${code}")
            LoadingLottieDialog.dismissDogLoadingDialog()
        }
    }

    override fun onGetChatListFailure(message: String) {
        Log.d("ChatList 오류", "오류: $message")
        LoadingLottieDialog.dismissDogLoadingDialog()
    }
}
