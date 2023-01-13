package com.starters.hsge.presentation.main.chat.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentChatBinding
import com.starters.hsge.domain.model.ChatListInfo
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.LoadingDialog
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.chat.chat.adapter.ChatListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat) {

    private val chatViewModel: ChatViewModel by viewModels()

    private lateinit var chatListAdapter: ChatListAdapter

    private var likedPeopleList = mutableListOf<ChatListInfo>()
    private var chatList = mutableListOf<ChatListInfo>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeChatList()
    }

    override fun onStart() {
        super.onStart()
        getChatInfo()
    }

    private fun getChatInfo() {
        likedPeopleList.clear()
        chatList.clear()
        chatViewModel.getChatList()
    }

    private fun observeChatList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                chatViewModel.chatListInfo.collect { state ->
                    when(state) {
                        is ChatState.Loading -> {
                            LoadingDialog.showDogLoadingDialog(requireContext())
                        }
                        is ChatState.Failure -> {
                            LoadingDialog.dismissDogLoadingDialog()
                            showToast("잠시 후 다시 시도해주세요")
                            Timber.d("!!실패")
                        }
                        is ChatState.Success -> {
                            LoadingDialog.dismissDogLoadingDialog()
                            Timber.d("!!성공")
                            for (element in state.data) {
                                if (element.active) { // active
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
                                    // 화면 밖으로 나갔다가 돌아왔을 때 invisible 처리가 안되는 문제
                                    ivChatEmpty.visibility = View.INVISIBLE
                                    tvChatEmptyTitle.visibility = View.INVISIBLE
                                    tvChatEmptySubtitle.visibility = View.INVISIBLE
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

                            // inactive_좋아요
                            chatListAdapter = ChatListAdapter(likedPeopleList, itemClickListener = {
                                goneBtmNav()
                            })

                            binding.chatRvLikedPeople.layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            binding.chatRvLikedPeople.adapter = chatListAdapter
                            Timber.d("likeList $likedPeopleList")

                            // active_채팅
                            chatListAdapter = ChatListAdapter(chatList, itemClickListener = {
                                goneBtmNav()
                            })

                            binding.chatRvChatList.layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            binding.chatRvChatList.adapter = chatListAdapter
                            Timber.d("chatList $chatList")
                        }
                        is ChatState.Initial -> {
                        }
                    }
                }
            }
        }
    }

    private fun goneBtmNav() {
        (activity as MainActivity).binding.navigationMain.visibility = View.GONE
    }
}
