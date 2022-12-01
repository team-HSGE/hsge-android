package com.starters.hsge.presentation.register.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.R
import com.starters.hsge.databinding.ItemProfileIconBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.base.BaseFragment.Companion.prefs

class UserProfileIconAdapter(private var userIconList: List<Int>)
    : RecyclerView.Adapter<UserProfileIconAdapter.UserProfileIconViewHolder>() {

    inner class UserProfileIconViewHolder(private val binding: ItemProfileIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resId: Int) {
            binding.icon = resId
            binding.executePendingBindings()
            itemView.setOnClickListener {
                prefs.edit().putInt("resId", resId).apply()
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_userProfileIconFragment_to_userImageFragment)
            }

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