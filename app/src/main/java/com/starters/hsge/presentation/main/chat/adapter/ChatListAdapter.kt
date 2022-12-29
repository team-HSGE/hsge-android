package com.starters.hsge.presentation.main.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.data.model.remote.response.ChatListResponse
import com.starters.hsge.databinding.ItemChatListBinding
import com.starters.hsge.databinding.ItemLikedPeopleBinding
import com.starters.hsge.presentation.main.chat.ChatFragmentDirections

class ChatListAdapter(private var chatListResponse: List<ChatListResponse?>?,
                      private val itemClickListener: () -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            1 -> { // chat active
                val bind =
                    ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ChatListViewHolder(bind)

            }
            else -> { // chat Inactive
                val bind =
                    ItemLikedPeopleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                LikedPeopleViewHolder(bind)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (chatListResponse?.get(position)?.active) {
            true -> {
                chatListResponse?.get(position)?.let { (holder as ChatListViewHolder).bind(it) }
            }
            false -> {
                chatListResponse?.get(position)?.let { (holder as LikedPeopleViewHolder).bind(it) }
            }
            else -> return
        }
    }

    override fun getItemCount(): Int {
        return chatListResponse?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        var active = 0
        if (!chatListResponse.isNullOrEmpty()) {
            active = if (chatListResponse?.get(position)?.active == true) 1 else 0
        }
        return active
    }

   inner class ChatListViewHolder(private val binding: ItemChatListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatListResponse) {
            with(binding) {
                chat.userIcon.let { chatListIvPersonIcon.setImageResource(it) }
                chatListTvPersonName.text = chat.nickname
                // TODO : date 연결
                //chatListTvDateBefore.text = "${chat.date}일 전"
                chatListTvMessage.text = chat.message
            }
            itemView.setOnClickListener {
                val action = ChatFragmentDirections.actionChatFragmentToChatRoomFragment(chat)
                it.findNavController().navigate(action)
                itemClickListener.invoke()
            }
        }
    }

    inner class LikedPeopleViewHolder(private val binding: ItemLikedPeopleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatListResponse) {
            with(binding) {
                chat.userIcon.let { likedPeopleIvIcon.setImageResource(it) }
                likedPeopleTvNickName.text = chat.nickname
            }

            itemView.setOnClickListener {
                val action = ChatFragmentDirections.actionChatFragmentToChatRoomFragment(chat)
                it.findNavController().navigate(action)
                itemClickListener.invoke()
            }
        }
    }
}