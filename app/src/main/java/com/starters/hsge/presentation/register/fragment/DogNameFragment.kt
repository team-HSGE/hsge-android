package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogNameBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DogNameFragment : BaseFragment<FragmentDogNameBinding>(R.layout.fragment_dog_name) {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTextWatcher()
        initListener()
        showDogNameText()
        setNavigation()
    }

    private fun setTextWatcher() {
        binding.edtDogName.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnNext.isEnabled = !binding.edtDogName.text.isNullOrBlank()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogNameFragment_to_dogPhotoFragment)

            lifecycleScope.launch {
                // datastore에 저장
                registerViewModel.saveDogName(binding.edtDogName.text.toString())
            }
        }
    }

    private fun showDogNameText() {
        lifecycleScope.launch {
            if (registerViewModel.fetchDogName().first().isNotEmpty()) {
                binding.edtDogName.setText(registerViewModel.fetchDogName().first())
            }
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}