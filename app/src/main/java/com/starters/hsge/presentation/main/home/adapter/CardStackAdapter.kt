package com.starters.hsge.presentation.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.starters.hsge.R
import com.starters.hsge.data.model.DogCard
import com.starters.hsge.databinding.ItemCardBinding


class CardStackAdapter(val context: Context, private val items: List<DogCard>) : RecyclerView.Adapter<CardStackAdapter.CardViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        //val view: View = inflater.inflate(R.layout.item_card, parent, false)

        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(dog: DogCard){
            with(binding){
                tvDogName.text = dog.dogName
                tvDogAge.text = dog.dogAge
                tvDogBreed.text = dog.dogBreed
                Glide.with(itemView).load(dog.imgUrl).into(ivDogPhoto)
                dog.tag.tagLike.forEach{
                    chipGroupLike.addLikeChip(context ,it)
                }
                dog.tag.tagDisLike.forEach {
                    chipGroupDislike.addDislikeChip(context, it)
                }
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