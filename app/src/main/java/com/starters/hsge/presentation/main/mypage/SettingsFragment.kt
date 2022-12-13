package com.starters.hsge.presentation.main.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentSettingsBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.LogoutDialogFragment
import com.starters.hsge.presentation.login.LoginActivity
import com.starters.hsge.presentation.main.MainActivity

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()

        binding.settingWithdrawalSection.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_withdrawalFragment)
        }

        binding.settingLogoutSection.setOnClickListener {
            val dialog = LogoutDialogFragment()

            dialog.setButtonClickListener(object: LogoutDialogFragment.OnButtonClickListener {
                override fun onCancelBtnClicked() {
                    // 취소 버튼 클릭했을 때 처리
                }

                override fun onLogoutBtnClicked() {
                    // 확인 버튼 클릭했을 때 처리
                    prefs.edit().clear().apply()
                    moveToLoginActivity()
                    Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
                }
            })

            dialog.show(childFragmentManager, "CustomDialog")
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }

    private fun moveToLoginActivity() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        ActivityCompat.finishAffinity(requireActivity())
        startActivity(intent)
    }
}