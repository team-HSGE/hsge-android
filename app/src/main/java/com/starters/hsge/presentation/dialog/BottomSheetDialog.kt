package com.starters.hsge.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentBottomSheetDialogBinding

class BottomSheetDialog(private val list: List<String>) : BottomSheetDialogFragment() {

    private lateinit var adapter: BottomSheetAdapter
    private lateinit var binding: FragmentBottomSheetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottom_sheet_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initRecyclerView(list)

    }

    private fun initRecyclerView(list: List<String>) {
        adapter = BottomSheetAdapter(list)
        binding.rvBottomSheet.layoutManager = LinearLayoutManager(context)
        binding.rvBottomSheet.adapter = adapter
    }


    companion object {
        const val TAG = "BottomSheetDialog"
    }

}