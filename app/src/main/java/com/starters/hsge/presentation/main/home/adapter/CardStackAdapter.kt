package com.starters.hsge.presentation.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.starters.hsge.R
import com.starters.hsge.data.model.remote.response.DogCard
import com.starters.hsge.databinding.ItemCardBinding


class CardStackAdapter(val context: Context, private val items: List<DogCard>) : RecyclerView.Adapter<CardStackAdapter.CardViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {

        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CardViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dog: DogCard) {
            with(binding) {
                tvDogName.text = dog.name
                tvDogAge.text = " I ${dog.age} I "
                tvDogBreed.text = dog.breed

                if (dog.sex == "남") {
                    ivDogSex.setImageResource(R.drawable.ic_gender_male_white)
                } else {
                    ivDogSex.setImageResource(R.drawable.ic_gender_female_white)
                }

                if (!dog.isNeuter) chipDogNeuter.visibility = View.GONE
                Glide.with(itemView).load(dog.picture).into(ivDogPhoto)

                binding.chipGroupLike.apply {
                    removeAllViewsInLayout()
                }

                binding.chipGroupDislike.apply {
                    removeAllViewsInLayout()
                }

                dog.tag.tagLike.forEach {
                    chipGroupLike.addLikeChip(context, it)
                }

                dog.tag.tagDisLike.forEach {
                    chipGroupDislike.addDisLikeChip(context, it)
                }
            }
        }
    }

    private fun ChipGroup.addLikeChip(context: Context, label: String) {
        Chip(context).apply {
            textSize = 14F
            setChipIconResource(R.drawable.ic_chip_like)
            text = label
            setChipBackgroundColorResource(R.color.light_yellow)
            chipIconSize = 34F
            iconStartPadding = 22F
            addView(this)
        }
    }

    private fun ChipGroup.addDisLikeChip(context: Context, label: String) {
        Chip(context).apply {
            textSize = 14F
            setChipIconResource(R.drawable.ic_chip_dislike)
            text = label
            setChipBackgroundColorResource(R.color.G200)
            chipIconSize = 34F
            iconStartPadding = 22F
            addView(this)
        }
    }
}
