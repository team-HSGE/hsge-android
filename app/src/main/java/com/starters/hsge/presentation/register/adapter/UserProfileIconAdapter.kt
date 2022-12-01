package com.starters.hsge.presentation.register.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.R
import com.starters.hsge.databinding.ItemProfileIconBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class UserProfileIconAdapter(private var userIconList: List<Int>)
    : RecyclerView.Adapter<UserProfileIconAdapter.UserProfileIconViewHolder>() {

    private lateinit var itemClickListener: OnItemClickLister

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

            holder.itemView.setOnClickListener {
                itemClickListener.onClick(it, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return userIconList.size
    }

    interface OnItemClickLister {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickLister) {
        this.itemClickListener = onItemClickListener
    }

}