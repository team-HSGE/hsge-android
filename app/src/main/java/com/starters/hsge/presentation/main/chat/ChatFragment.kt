package com.starters.hsge.presentation.main.chat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.starters.hsge.R
import com.starters.hsge.data.model.LikedPeople
import com.starters.hsge.databinding.FragmentChatBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.chat.adapter.LikedPeopleAdapter

class ChatFragment : BaseFragment<FragmentChatBinding>(R.layout.fragment_chat) {

    private lateinit var adapter: LikedPeopleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val likedPeopleList = listOf(
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
            ),
            LikedPeople(
                R.drawable.ic_user_icon_test,
                "덴마크당나귀"
            )
        )

        initRecyclerView(likedPeopleList)


    }

    private fun initRecyclerView(list: List<LikedPeople>) {
        binding.chatRvLikedPeople.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = LikedPeopleAdapter(list)
        binding.chatRvLikedPeople.adapter = adapter
    }
}