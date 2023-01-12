package com.starters.hsge.presentation.main.chat.chatroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.data.model.remote.response.Message
import com.starters.hsge.databinding.ItemMessageReceivedBinding
import com.starters.hsge.databinding.ItemMessageSentBinding

class MessageListAdapter(private val userId: Long, private val viewModel: ChatRoomViewModel) :
    ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }

    class SentViewHolder(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ChatRoomViewModel, msg: Message) {
            binding.messageInfo = msg
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    class ReceivedViewHolder(private val binding: ItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ChatRoomViewModel, msg: Message) {
            binding.messageInfo = msg
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    // 뷰타입 설정해준대로 동작
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId == userId) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_MESSAGE_SENT -> {
                val binding = ItemMessageSentBinding.inflate(layoutInflater, parent, false)
                SentViewHolder(binding)
            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                val binding = ItemMessageReceivedBinding.inflate(layoutInflater, parent, false)
                ReceivedViewHolder(binding)
            }
            else -> {
                throw Exception("Error reading holder type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentViewHolder).bind(
                viewModel,
                getItem(position)
            )
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedViewHolder).bind(
                viewModel,
                getItem(position)
            )
        }
    }
}

class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.timeStamp == newItem.timeStamp
    }
}