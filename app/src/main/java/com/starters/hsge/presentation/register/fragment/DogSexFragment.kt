package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogSexBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogSexFragment : BaseFragment<FragmentDogSexBinding>(R.layout.fragment_dog_sex) {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        setNavigation()

    }

    private fun initListener() {

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.rbtn_male -> {
                    registerViewModel.dogSex = "남자"
                    setButtonEnable()
                }
                R.id.rbtn_female -> {
                    registerViewModel.dogSex = "여자"
                    setButtonEnable() // 멘토님꼐 물어보기 ^__^
                }
            }
        }

        binding.checkboxNeuter.setOnCheckedChangeListener { _, isChecked ->
            registerViewModel.dogNeuter = isChecked
        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogSexFragment_to_dogAgeFragment)
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setButtonEnable() {
        binding.btnNext.isEnabled = !registerViewModel.dogSex.isNullOrEmpty()
    }
}