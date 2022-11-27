package com.starters.hsge.presentation.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.starters.hsge.databinding.ItemBottomSheetBinding

class BottomSheetAdapter(private var contentsList: List<String>) :
    RecyclerView.Adapter<BottomSheetAdapter.BottomSheetViewHolder>(
) {

    inner class BottomSheetViewHolder(private val binding: ItemBottomSheetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(content: String) {
            binding.contents = content
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val binding =
            ItemBottomSheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BottomSheetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        contentsList.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int {
        return contentsList.size
    }
}