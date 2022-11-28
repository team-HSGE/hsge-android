package com.starters.hsge.presentation.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

        }
    }
}
