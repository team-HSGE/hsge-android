package com.starters.hsge.presentation.main.chat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.starters.hsge.R
import com.starters.hsge.data.model.ChatList
import com.starters.hsge.data.model.LikedPeople
import com.starters.hsge.databinding.FragmentChatBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.chat.adapter.ChatListAdapter
import com.starters.hsge.presentation.main.chat.adapter.LikedPeopleAdapter

class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat) {

    private lateinit var likedPeopleAdapter: LikedPeopleAdapter
    private lateinit var chatListAdapter: ChatListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val likedPeopleList = listOf(
            LikedPeople(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀안녕하세요"
            ),
            LikedPeople(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeople(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeople(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeople(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeople(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeople(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            ),
            LikedPeople(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            )
        )

        val chatList = listOf(
            ChatList(
                R.drawable.ic_dog_profile_9,
                "응콩",
                4,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),
            ChatList(
                R.drawable.ic_dog_profile_7,
                "예빈",
                1,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),
            ChatList(
                R.drawable.ic_dog_profile_14,
                "서윤",
                2,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),ChatList(
                R.drawable.ic_dog_profile_1,
                "석주",
                4,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),
            ChatList(
                R.drawable.ic_dog_profile_13,
                "정은",
                7,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),ChatList(
                R.drawable.ic_dog_profile_10,
                "김인",
                1,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),
            ChatList(
                R.drawable.ic_dog_profile_3,
                "태민",
                5,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),ChatList(
                R.drawable.ic_dog_profile_15,
                "화진",
                2,
                "안녕하세요, 나이가 어떻게 되세요?"
            ),
            ChatList(
                R.drawable.ic_dog_profile_4,
                "영선",
                3,
                "안녕하세요, 나이가 어떻게 되세요?"
            )

        )

        likePeopleRecyclerView(likedPeopleList)
        chatListRecyclerView(chatList)


    }

    private fun likePeopleRecyclerView(list: List<LikedPeople>) {
        binding.chatRvLikedPeople.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        likedPeopleAdapter = LikedPeopleAdapter(list)
        binding.chatRvLikedPeople.adapter = likedPeopleAdapter
    }

    private fun chatListRecyclerView(list: List<ChatList>){
        binding.chatRvChatList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chatListAdapter = ChatListAdapter(list)
        binding.chatRvChatList.adapter = chatListAdapter
    }
}