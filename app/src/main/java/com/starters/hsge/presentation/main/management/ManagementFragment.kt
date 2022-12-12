package com.starters.hsge.presentation.main.management

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentManagementBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.CustomDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementFragment : BaseFragment<FragmentManagementBinding>(R.layout.fragment_management) {

    private val managementViewModel: ManagementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customDecoration = CustomDecoration(1f, 0f, Color.parseColor("#EFEFEF"))
        binding.rvDogList.addItemDecoration(customDecoration)

        managementViewModel.myDogList.observe(viewLifecycleOwner) {
            val adapter = it?.let { it1 -> DogListAdapter(it1) }
            binding.rvDogList.layoutManager = LinearLayoutManager(context)
            binding.rvDogList.adapter = adapter
        }
    }
}
