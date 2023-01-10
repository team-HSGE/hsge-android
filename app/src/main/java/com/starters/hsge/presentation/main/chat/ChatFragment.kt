package com.starters.hsge.presentation.main.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.chatListInterface
import com.starters.hsge.data.model.remote.response.ChatListResponse
import com.starters.hsge.data.service.ChatListService
import com.starters.hsge.databinding.FragmentChatBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.chat.adapter.ChatListAdapter

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
        //LoadingDialog.showDogLoadingDialog(requireContext())
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

            if (chatList.isEmpty() && likedPeopleList.isEmpty()) {
                with(binding) {
                    ivChatEmpty.visibility = View.VISIBLE
                    tvChatEmptyTitle.visibility = View.VISIBLE
                    tvChatEmptySubtitle.visibility = View.VISIBLE
                }
            } else {
                with(binding) {
                    tvChatLikedPeopleTitle.visibility = View.VISIBLE
                    tvChatChatListTitle.visibility = View.VISIBLE
                }
            }

            if (chatList.isEmpty() && likedPeopleList.isNotEmpty()) {
                with(binding) {
                    ivChatListEmpty.visibility = View.VISIBLE
                    tvChatListEmptyTitle.visibility = View.VISIBLE
                    tvChatListEmptySubtitle.visibility = View.VISIBLE
                }
            }

//            if (likedPeopleList.isEmpty()) {
//                binding.ivLikedListEmpty.visibility = View.VISIBLE
//            }

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
            //LoadingDialog.dismissDogLoadingDialog()
        } else {
            Log.d("ChatList 오류", "Error code : ${code}")
            //LoadingDialog.dismissDogLoadingDialog()
            showToast("잠시 후 다시 시도해주세요")
        }
    }

    override fun onGetChatListFailure(message: String) {
        Log.d("ChatList 오류", "오류: $message")
        //LoadingDialog.dismissDogLoadingDialog()
        showToast("잠시 후 다시 시도해주세요")
    }
}
