package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogBreedBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DogBreedFragment : BaseFragment<FragmentDogBreedBinding>(R.layout.fragment_dog_breed) {

    private lateinit var breedBottomSheet: BottomSheetDialog
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        setNavigation()
    }

    private fun initListener() {
        binding.tvDogBreed.setOnClickListener {
            breedBottomSheet = BottomSheetDialog(registerViewModel.getDogBreed2().map { it.kind })
            breedBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
            breedBottomSheet.setBottomSheetClickListener(object :
                BottomSheetDialog.BottomSheetClickListener {
                override fun onContentClick(content: String) {
                    registerViewModel.dogBreed = content
                    showDogBreed()
                    setButtonEnable()
                }
            })

//            lifecycleScope.launch {
//                breedBottomSheet = BottomSheetDialog(registerViewModel.getDogBreed().value!!.map { it.value })
//            }

        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogBreedFragment_to_dogLikeTagFragment)
        }
    }

    private fun showDogBreed() {
        binding.tvDogBreed.text.let {
            binding.tvDogBreed.text = registerViewModel.dogBreed
        }
    }

    private fun setButtonEnable() {
        binding.btnNext.isEnabled = !registerViewModel.dogBreed.isNullOrEmpty()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}