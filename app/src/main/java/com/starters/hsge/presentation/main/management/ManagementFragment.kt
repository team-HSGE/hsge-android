package com.starters.hsge.presentation.main.management

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentManagementBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.CustomDecoration
import com.starters.hsge.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ManagementFragment : BaseFragment<FragmentManagementBinding>(R.layout.fragment_management) {

    private val managementViewModel: ManagementViewModel by viewModels()

    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initRecyclerView()
        getMyDogList()
        initListener()
        setNavigation()
        observeMyDogList()

    }

    private fun getMyDogList() {
        managementViewModel.getMyDogList()
    }

    private fun observeMyDogList() {
        Timber.d("observing")
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                managementViewModel.myDogList.collect { state ->
                    when (state) {
                        is ManagementState.Loading -> {
                            //TODO: 로딩 다이얼로그 띄우기
                        }
                        is ManagementState.Failure -> {
                            //TODO: 로딩 다이얼로그 해제
                        }
                        is ManagementState.Success -> {
                            val customDecoration = CustomDecoration(1f, 0f, Color.parseColor("#EFEFEF"))
                            binding.rvDogList.addItemDecoration(customDecoration)
                            val adapter = DogListAdapter(state.data)
                            Timber.d("!!반려견리스트 ${state.data}")
                            binding.rvDogList.adapter = adapter
                        }
                        is ManagementState.Initial -> {
                        }
                    }
                }
            }
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

    private fun visibleBtmNav() {
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }
}

