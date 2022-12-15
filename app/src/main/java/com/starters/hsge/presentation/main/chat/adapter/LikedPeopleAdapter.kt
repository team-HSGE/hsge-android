package com.starters.hsge.presentation.main.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.data.model.remote.response.LikedPeopleResponse
import com.starters.hsge.databinding.ItemLikedPeopleBinding

class LikedPeopleAdapter(private var likedPeopleResponseList: List<LikedPeopleResponse>) : RecyclerView.Adapter<LikedPeopleAdapter.LikedPeopleViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedPeopleViewHolder {
        val binding = ItemLikedPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LikedPeopleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LikedPeopleViewHolder, position: Int) {
        holder.bind(likedPeopleResponseList[position])
    }

    override fun getItemCount(): Int {
        return likedPeopleResponseList.size
    }

    class LikedPeopleViewHolder(private val binding: ItemLikedPeopleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(people: LikedPeopleResponse){
            with(binding){
                likedPeopleIvIcon.setImageResource(people.iconId)
                likedPeopleTvNickName.text = people.nickName
            }
        }
    }
}