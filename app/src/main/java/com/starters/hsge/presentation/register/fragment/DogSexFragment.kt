package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogSexBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DogSexFragment : BaseFragment<FragmentDogSexBinding>(R.layout.fragment_dog_sex) {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            registerViewModel.saveDogSex("")
        }

        updateCheckedGender()
        updateNeuterCheckBox()
        initListener()
        setNavigation()

    }

    private fun initListener() {

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.rbtn_male -> {
                    lifecycleScope.launch {
                        registerViewModel.saveDogSex("남")
                    }
                }
                R.id.rbtn_female -> {
                    lifecycleScope.launch {
                        registerViewModel.saveDogSex("여")
                    }
                }
            }
        }

        binding.checkboxNeuter.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                registerViewModel.saveDogNeuter(isChecked)
            }
        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogSexFragment_to_dogAgeFragment)
        }
    }

    private fun updateCheckedGender() {
        registerViewModel.fetchDogSex().asLiveData().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                when (it) {
                    "남" -> binding.rbtnMale.isChecked = true
                    "여" -> binding.rbtnFemale.isChecked = true
                }
                binding.btnNext.isEnabled = true
            }
        }
    }

    private fun updateNeuterCheckBox() {
        lifecycleScope.launch {
            binding.checkboxNeuter.isChecked = registerViewModel.fetchDogNeuter().first()
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}