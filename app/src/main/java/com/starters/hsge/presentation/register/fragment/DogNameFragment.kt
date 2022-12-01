package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogNameBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DogNameFragment : BaseFragment<FragmentDogNameBinding>(R.layout.fragment_dog_name) {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTextWatcher()
        initListener()

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
                .navigate(R.id.action_dogNameFragment_to_dogSexFragment)

            registerViewModel.dogName = binding.edtDogName.text.toString()
        }
    }
}