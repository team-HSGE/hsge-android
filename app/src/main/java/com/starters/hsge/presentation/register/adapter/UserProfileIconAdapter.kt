package com.starters.hsge.presentation.register.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.databinding.ItemProfileIconBinding

class UserProfileIconAdapter(
    private var userIconList: List<Int>
) : RecyclerView.Adapter<UserProfileIconAdapter.UserProfileIconViewHolder>() {


    inner class UserProfileIconViewHolder(private val binding: ItemProfileIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resId: Int) {
            binding.icon = resId
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileIconViewHolder {
        val binding =
            ItemProfileIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserProfileIconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserProfileIconViewHolder, position: Int) {
        userIconList.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int {
        return userIconList.size
    }
}