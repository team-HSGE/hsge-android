package com.starters.hsge.presentation.main.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.data.model.ChatList
import com.starters.hsge.databinding.ItemChatListBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class ChatListAdapter(private var chatList: List<ChatList>) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val binding = ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class ChatListViewHolder(private val binding: ItemChatListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(chat: ChatList){
            with(binding){
                chatListIvPersonIcon.setImageResource(chat.iconId)
                chatListTvPersonName.text = chat.nickName
                chatListTvDateBefore.text = "${chat.date}일 전"
                chatListTvMessage.text = chat.chatMessage
            }
        }
    }
}