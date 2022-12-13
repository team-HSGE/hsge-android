package com.starters.hsge.presentation.main.management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.data.model.remote.response.MyDogResponse
import com.starters.hsge.databinding.ItemDogListBinding

class DogListAdapter(private var dogList: List<MyDogResponse>) : RecyclerView.Adapter<DogListAdapter.DogListViewHolder>() {

    inner class DogListViewHolder(private val binding: ItemDogListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MyDogResponse) {

            binding.myDogList = data
            binding.executePendingBindings()

            itemView.setOnClickListener { view ->
                val action =
                    ManagementFragmentDirections.actionManagementFragmentToDogProfileEditFragment(data)
                view.findNavController().navigate(action)

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