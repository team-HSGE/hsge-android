package com.starters.hsge.presentation.register.fragment

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
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

        setNavigation()
        initListener()
        setListenerToEditText()
    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {

            if(binding.edtUserNickName.text.isNullOrBlank()) {
                binding.edtUserNickName.isEnabled = true
            } else {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_userNickNameFragment_to_userImageFragment)
            }

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
