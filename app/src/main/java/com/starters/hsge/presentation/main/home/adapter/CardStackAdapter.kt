package com.starters.hsge.presentation.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.starters.hsge.R
import com.starters.hsge.data.model.DogCard


class CardStackAdapter(val context: Context, private val items: List<DogCard>) : RecyclerView.Adapter<CardStackAdapter.CardViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun binding(dog: DogCard){
            itemView.findViewById<TextView>(R.id.tv_dog_name).text = dog.dogName
            itemView.findViewById<TextView>(R.id.tv_dog_breed).text = dog.dogBreed
            itemView.findViewById<TextView>(R.id.tv_dog_age).text = dog.dogAge
            Glide.with(itemView)
                .load(dog.imgUrl)
                .into(itemView.findViewById(R.id.iv_dog_photo))
            dog.tag.tagLike.size
            dog.tag.tagDisLike.size
            dog.tag.tagLike.forEach{
                itemView.findViewById<ChipGroup>(R.id.chip_group_like).addLikeChip(context, it)
            }
            dog.tag.tagDisLike.forEach{
                itemView.findViewById<ChipGroup>(R.id.chip_group_dislike).addDislikeChip(context, it)
            }
        }
        fun ChipGroup.addLikeChip(context: Context, label: String){

            Chip(context).apply {
                textSize = 12F
                setChipIconResource(R.drawable.chip_like)
                text = label
                setChipBackgroundColorResource(R.color.light_yellow)

                chipIconSize = 34F
                iconStartPadding = 22F

                addView(this)
            }
        }

        fun ChipGroup.addDislikeChip(context: Context, label: String){

            Chip(context).apply {
                textSize = 12F
                setChipIconResource(R.drawable.chip_dislike)
                text = label
                setChipBackgroundColorResource(R.color.G200)

                chipIconSize = 34F
                iconStartPadding = 22F

                addView(this)
            }
        }


    }
}
