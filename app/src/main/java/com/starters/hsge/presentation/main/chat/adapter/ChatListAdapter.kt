package com.starters.hsge.presentation.main.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.data.model.remote.response.ChatListResponse
import com.starters.hsge.databinding.ItemChatListBinding

class ChatListAdapter(private var chatListResponse: List<ChatListResponse>) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val binding = ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind(chatListResponse[position])
    }

    override fun getItemCount(): Int {
        return chatListResponse.size
    }

    class ChatListViewHolder(private val binding: ItemChatListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(chat: ChatListResponse){
            with(binding){
                chatListIvPersonIcon.setImageResource(chat.iconId)
                chatListTvPersonName.text = chat.nickName
                chatListTvDateBefore.text = "${chat.date}일 전"
                chatListTvMessage.text = chat.chatMessage
            }
        }
    }
}