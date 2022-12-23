package com.starters.hsge.presentation.main.chatroom

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.databinding.ItemMessageReceivedBinding
import com.starters.hsge.databinding.ItemMessageSentBinding

class MessageListAdapter(private val userId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }


    class ReceivedViewHolder(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }


    class SentViewHolder(private val binding: ItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    // 뷰타입 설정해준대로 동작
    override fun getItemViewType(position: Int): Int {
        return getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}