package com.starters.hsge.presentation.main.partner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.R
import com.starters.hsge.databinding.ItemDogListBinding

class PartnerDogsAdapter(private val dogList: List<PartnerDogs>) :
    RecyclerView.Adapter<PartnerDogsAdapter.PartnerDogsViewHolder>() {

    inner class PartnerDogsViewHolder(private val binding: ItemDogListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PartnerDogs) {
            with(binding) {
                tvDogName.text = data.name
                tvDogAge.text = data.age
                tvDogBreed.text = data.breed
            }
            itemView.setOnClickListener {
                Navigation.findNavController(binding.root).navigate(R.id.action_partnerDogsFragment_to_specificDogFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerDogsViewHolder {
        val binding =
            ItemDogListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PartnerDogsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartnerDogsViewHolder, position: Int) {
        dogList.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int {
        return dogList.size
    }
}
