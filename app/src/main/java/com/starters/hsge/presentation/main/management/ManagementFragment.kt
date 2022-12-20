package com.starters.hsge.presentation.main.management

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentManagementBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.CustomDecoration
import com.starters.hsge.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagementFragment : BaseFragment<FragmentManagementBinding>(R.layout.fragment_management) {

    private val managementViewModel: ManagementViewModel by viewModels()

    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customDecoration = CustomDecoration(1f, 0f, Color.parseColor("#EFEFEF"))
        binding.rvDogList.addItemDecoration(customDecoration)

        initRecyclerView()
        initListener()
        setNavigation()

    }

    private fun initRecyclerView() {
        managementViewModel.myDogList.observe(viewLifecycleOwner) {
            val adapter = it?.let { it1 -> DogListAdapter(it1) }
            binding.rvDogList.layoutManager = LinearLayoutManager(context)
            binding.rvDogList.adapter = adapter
        }
    }

    private fun initListener() {
        binding.fabAddDog.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_managementFragment_to_addDogProfileFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
                visibleBtmNav()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }
}
