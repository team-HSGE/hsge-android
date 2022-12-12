package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogAgeBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DogAgeFragment @Inject constructor(
) : BaseFragment<FragmentDogAgeBinding>(R.layout.fragment_dog_age) {

    private lateinit var ageBottomSheet: BottomSheetDialog
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        setNavigation()
        upDateDogAgeText()

    }

    private fun initListener() {
        binding.tvDogAge.setOnClickListener {
            registerViewModel.ageMap.observe(viewLifecycleOwner) { age ->
                if (age != null) {
                    ageBottomSheet = BottomSheetDialog(age.keys.toList())
                    ageBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
                    ageBottomSheet.setBottomSheetClickListener(object :
                        BottomSheetDialog.BottomSheetClickListener {
                        override fun onContentClick(content: String) {
                            lifecycleScope.launch {
                                age[content]?.let { it -> registerViewModel.saveDogAge(it) }
                                registerViewModel.saveDogAgeForView(content)
                                Log.d("보내는 값", "${age[content]}")
                            }
                        }
                    })
                }
            }

            // 로컬데이터 띄우기 (테스트용)
//            ageBottomSheet = BottomSheetDialog(registerViewModel.testDogAgeToLocal().map{ it.age })
//            ageBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
//                    ageBottomSheet.setBottomSheetClickListener(object :
//                        BottomSheetDialog.BottomSheetClickListener {
//                        override fun onContentClick(content: String) {
//                            lifecycleScope.launch {
//                                registerViewModel.saveDogAgeForView(content)
//                                registerViewModel.saveDogAge("MIX")
//                                Log.d("나이 저장디어야디", "${registerViewModel.fetchDogAgeForView().first()}, ${registerViewModel.fetchDogAge().first()} ")
//                            }
//                        }
//                    })
        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogAgeFragment_to_dogBreedFragment)
        }
    }

    private fun upDateDogAgeText() {
        registerViewModel.fetchDogAgeForView().asLiveData().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.tvDogAge.text = it
                binding.btnNext.isEnabled = true
            }
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}