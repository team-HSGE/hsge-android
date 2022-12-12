package com.starters.hsge.presentation.main.management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.databinding.ItemDogListBinding

class DogListAdapter(private var dogList: List<Doggi>) : RecyclerView.Adapter<DogListAdapter.DogListViewHolder>() {

    inner class DogListViewHolder(private val binding: ItemDogListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Doggi) {
            with(binding) {
                ivDogPhoto.setImageResource(data.dogPhoto)
                tvDogName.text = data.dogName
                ivDogSex.setImageResource(data.dogSex)
                tvDogAge.text = data.dogAge
                tvDogBreed.text = data.dogBreed
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogListViewHolder {
        val binding =
            ItemDogListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogListViewHolder, position: Int) {
        dogList.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int {
        return dogList.size
    }
}