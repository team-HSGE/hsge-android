package com.starters.hsge.presentation.register.fragment

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserNickNameBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class UserNickNameFragment : BaseFragment<FragmentUserNickNameBinding>(R.layout.fragment_user_nick_name) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTextWatcher()
        setNavigation()
        initListener()
        //setListenerToEditText()
    }

    private fun setTextWatcher() {
        binding.edtUserNickName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnNext.isEnabled = !binding.edtUserNickName.text.isNullOrBlank()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_userNickNameFragment_to_userImageFragment)
        }
    }

    // 엔터 시 포커스 제거 & 키보드 내리기
    private fun setListenerToEditText() {
        binding.edtUserNickName.setOnKeyListener { view, keyCode, event ->
            // Enter Key Action
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                view.clearFocus()
                // 키패드 내리기
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.edtUserNickName.windowToken, 0)

                return@setOnKeyListener true
            }

            false
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
