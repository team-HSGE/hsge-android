package com.starters.hsge.presentation.main.chat

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.starters.hsge.R
import com.starters.hsge.data.model.remote.response.ChatListResponse
import com.starters.hsge.data.model.remote.response.LikedPeopleResponse
import com.starters.hsge.databinding.FragmentChatBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.chat.adapter.ChatListAdapter
import com.starters.hsge.presentation.main.chat.adapter.LikedPeopleAdapter

class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat) {

    private lateinit var likedPeopleAdapter: LikedPeopleAdapter
    private lateinit var chatListAdapter: ChatListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val likedPeopleResponseLists = listOf(
            LikedPeopleResponse(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀안녕하세요"
            ),
            LikedPeopleResponse(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeopleResponse(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeopleResponse(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeopleResponse(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeopleResponse(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeopleResponse(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeopleResponse(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            )
        )

        val chatListResponses = listOf(
            ChatListResponse(
                R.drawable.ic_dog_profile_9,
                "응콩",
                4,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),
            ChatListResponse(
                R.drawable.ic_dog_profile_7,
                "예빈",
                1,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),
            ChatListResponse(
                R.drawable.ic_dog_profile_14,
                "서윤",
                2,
                "안녕하세요, 나이가 어떻게 되세요?"
            ), ChatListResponse(
                R.drawable.ic_dog_profile_1,
                "석주",
                4,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),
            ChatListResponse(
                R.drawable.ic_dog_profile_13,
                "정은",
                7,
                "안녕하세요, 나이가 어떻게 되세요?"
            ), ChatListResponse(
                R.drawable.ic_dog_profile_10,
                "김인",
                1,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),
            ChatListResponse(
                R.drawable.ic_dog_profile_3,
                "태민",
                5,
                "안녕하세요, 나이가 어떻게 되세요?"
            ), ChatListResponse(
                R.drawable.ic_dog_profile_15,
                "화진",
                2,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),
            ChatListResponse(
                R.drawable.ic_dog_profile_4,
                "영선",
                3,
                "안녕하세요, 나이가 어떻게 되세요?"
            )

        )

        likePeopleRecyclerView(likedPeopleResponseLists)
        chatListRecyclerView(chatListResponses)

    }

    private fun likePeopleRecyclerView(list: List<LikedPeopleResponse>) {
        binding.chatRvLikedPeople.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        likedPeopleAdapter = LikedPeopleAdapter(list, itemClickListener = {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_chatFragment_to_chatRoomFragment, bundleOf(ACTIVE to 0) )
            goneBtmNav()
        })
        binding.chatRvLikedPeople.adapter = likedPeopleAdapter
    }

    private fun chatListRecyclerView(list: List<ChatListResponse>){
        binding.chatRvChatList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatListAdapter = ChatListAdapter(list, itemClickListener = {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_chatFragment_to_chatRoomFragment, bundleOf(ACTIVE to 1))
            goneBtmNav()
        })
        binding.chatRvChatList.adapter = chatListAdapter
    }

    private fun goneBtmNav(){ (activity as MainActivity).binding.navigationMain.visibility = View.GONE }

    companion object {
        const val ACTIVE = "active"
    }
}