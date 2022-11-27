package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogBreedBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogBreedFragment : BaseFragment<FragmentDogBreedBinding>(R.layout.fragment_dog_breed) {

    private lateinit var ageBottomSheet: BottomSheetDialog
    private val registerViewModel:RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.tvDogBreed.setOnClickListener {
            ageBottomSheet = BottomSheetDialog(registerViewModel.getDogBreed().map { it.kind })
            ageBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
        }
    }
}