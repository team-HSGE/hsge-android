package com.starters.hsge.presentation.main.partner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.data.model.remote.response.MyDogResponse
import com.starters.hsge.databinding.ItemDogListBinding

class PartnerDogsAdapter(private val dogList: List<MyDogResponse>?) :
    RecyclerView.Adapter<PartnerDogsAdapter.PartnerDogsViewHolder>() {

    inner class PartnerDogsViewHolder(private val binding: ItemDogListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MyDogResponse) {
            binding.myDogList = data
            itemView.setOnClickListener { view ->
                val action = PartnerDogsFragmentDirections.actionPartnerDogsFragmentToSpecificDogFragment(data)
                view.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerDogsViewHolder {
        val binding =
            ItemDogListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PartnerDogsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartnerDogsViewHolder, position: Int) {
        dogList?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int {
        return dogList?.size ?: 0
    }
}
