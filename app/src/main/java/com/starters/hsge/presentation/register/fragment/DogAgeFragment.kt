package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogAgeBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogAgeFragment : BaseFragment<FragmentDogAgeBinding>(R.layout.fragment_dog_age) {

    private lateinit var ageBottomSheet: BottomSheetDialog
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        setNavigation()

    }

    private fun initListener() {
        binding.tvDogAge.setOnClickListener {
            registerViewModel.ageList.observe(viewLifecycleOwner) { age ->

                // value - key HashMap 생성
                val dogAgeHashMap = hashMapOf<String, String>()
                dogAgeHashMap[age.map { it.value }.toString()] = age.map { it.key }.toString()

                ageBottomSheet = BottomSheetDialog(age.map { it.value })
                ageBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
                ageBottomSheet.setBottomSheetClickListener(object :
                    BottomSheetDialog.BottomSheetClickListener {
                    override fun onContentClick(content: String) {
                        // HashMap에서 해당하는 key값 찾아서 넣어주기
                        registerViewModel.dogAge = dogAgeHashMap[content].toString()
                        showDogAge()
                        setButtonEnable()
                    }
                })
            }
        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogAgeFragment_to_dogBreedFragment)
        }
    }

    private fun showDogAge() {
        binding.tvDogAge.text.let {
            binding.tvDogAge.text = registerViewModel.dogAge
        }
    }

    private fun setButtonEnable() {
        binding.btnNext.isEnabled = !registerViewModel.dogAge.isNullOrEmpty()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}