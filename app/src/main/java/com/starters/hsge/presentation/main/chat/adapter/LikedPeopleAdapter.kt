package com.starters.hsge.presentation.main.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.data.model.LikedPeople
import com.starters.hsge.databinding.ItemLikedPeopleBinding

class LikedPeopleAdapter(private var likedPeopleList: List<LikedPeople>) : RecyclerView.Adapter<LikedPeopleAdapter.LikedPeopleViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedPeopleViewHolder {
        val binding = ItemLikedPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LikedPeopleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LikedPeopleViewHolder, position: Int) {
        holder.bind(likedPeopleList[position])
    }

    override fun getItemCount(): Int {
        return likedPeopleList.size
    }

    class LikedPeopleViewHolder(private val binding: ItemLikedPeopleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(people: LikedPeople){
            with(binding){
                likedPeopleIvIcon.setImageResource(people.iconId)
                likedPeopleTvNickName.text = people.nickName
            }
        }
    }
}