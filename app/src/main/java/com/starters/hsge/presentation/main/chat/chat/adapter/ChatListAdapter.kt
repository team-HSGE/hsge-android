package com.starters.hsge.presentation.main.chat.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.databinding.ItemChatListBinding
import com.starters.hsge.databinding.ItemLikedPeopleBinding
import com.starters.hsge.domain.model.ChatListInfo
import com.starters.hsge.presentation.main.chat.chat.ChatFragmentDirections

class ChatListAdapter(
    private var chatList: List<ChatListInfo?>?,
    private val itemClickListener: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> { // chat active
                val bind =
                    ItemChatListBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
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
        chatList?.let { chatList ->
            when (chatList[position]?.active) {
                true -> {
                    chatList[position]?.let { (holder as ChatListViewHolder).bind(it) }
                }
                false -> {
                    chatList[position]?.let { (holder as LikedPeopleViewHolder).bind(it) }
                }
                else -> return
            }
        }
    }

    override fun getItemCount(): Int {
        return chatList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        var active = 0
        if (!chatList.isNullOrEmpty()) {
            active = if (chatList?.get(position)?.active == true) 1 else 0
        }
        return active
    }

    inner class ChatListViewHolder(private val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatListInfo) {
            binding.chatListInfo = chat
            itemView.setOnClickListener {
                val action = ChatFragmentDirections.actionChatFragmentToChatRoomFragment(chat)
                it.findNavController().navigate(action)
                itemClickListener.invoke()
            }
        }
    }

    inner class LikedPeopleViewHolder(private val binding: ItemLikedPeopleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatListInfo) {
            binding.chatListInfo = chat
            itemView.setOnClickListener {
                val action = ChatFragmentDirections.actionChatFragmentToChatRoomFragment(chat)
                it.findNavController().navigate(action)
                itemClickListener.invoke()
            }
        }
    }
}