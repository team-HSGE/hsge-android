package com.starters.hsge.presentation.main.mypage.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.user.UserApiClient
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentSettingsBinding
import com.starters.hsge.data.interfaces.SettingsInterface
import com.starters.hsge.data.service.SettingsService
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BaseDialogFragment
import com.starters.hsge.presentation.login.LoginActivity
import com.starters.hsge.presentation.main.MainActivity
import timber.log.Timber

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings), SettingsInterface {

    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()
        initListener()
        checkNotification()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
                visibleBtmNav()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun moveToLoginActivity() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        ActivityCompat.finishAffinity(requireActivity())
        startActivity(intent)
    }

    override fun onDeleteFcmTokenSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            Log.d("FCM토큰 삭제", "성공!")

            prefs.edit().clear().apply()

            moveToLoginActivity()
            showToast("로그아웃 되었습니다.")
        }
    }

    override fun onDeleteFcmTokenFailure(message: String) {
        Log.d("FCM토큰 삭제 오류", "오류: $message")
    }

    private fun initListener(){
        binding.settingWithdrawalSection.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_withdrawalFragment)
        }

        binding.settingLogoutSection.setOnClickListener {
            val dialog = BaseDialogFragment("로그아웃 하시겠습니까?")

            dialog.setButtonClickListener(object: BaseDialogFragment.OnButtonClickListener {
                override fun onCancelBtnClicked() {
                    // 취소 버튼 클릭했을 때 처리
                }

                override fun onOkBtnClicked() {
                    // 확인 버튼 클릭했을 때 처리
                    UserApiClient.instance.logout { error ->
                        if (error != null) {
                            Log.d("로그아웃", "로그아웃 실패 : ${error}")
                            showToast("다시 시도해주세요.")
                        }else {
                            // access token & fcm token 날리기
                            SettingsService(this@SettingsFragment).tryDeleteFcmToken()
                        }
                    }
                }
            })
            dialog.show(childFragmentManager, "CustomDialog")
        }
    }

    private fun checkNotification() {
        val spAll = prefs.getBoolean("checkAll", true)
        val spChat = prefs.getBoolean("chatCheck", true)
        val spLike = prefs.getBoolean("likeCheck", true)
        val spWave = prefs.getBoolean("waveCheck", true)

        if (spAll){
            binding.swPushAlarmAll.isChecked = true
            binding.swPushAlarmLike.isChecked = true
            binding.swPushAlarmChat.isChecked = true
            binding.swPushAlarmWave.isChecked = true
        } else if (!spLike || !spWave || !spChat){
            binding.swPushAlarmAll.isChecked = false
        }

        if (spLike){
            binding.swPushAlarmLike.isChecked = true
        }
        if (spChat){
            binding.swPushAlarmChat.isChecked = true
        }
        if (spWave){
            binding.swPushAlarmWave.isChecked = true
        }

        binding.swPushAlarmAll.setOnClickListener(SwitchListener())
        binding.swPushAlarmLike.setOnClickListener(SwitchListener())
        binding.swPushAlarmChat.setOnClickListener(SwitchListener())
        binding.swPushAlarmWave.setOnClickListener(SwitchListener())
    }

    inner class SwitchListener : View.OnClickListener{
        override fun onClick(v: View?) {

            val likeCheck = binding.swPushAlarmLike
            val waveCheck = binding.swPushAlarmWave
            val chatCheck = binding.swPushAlarmChat
            val checkAll = binding.swPushAlarmAll

            when (v?.id){
                R.id.sw_push_alarm_all -> {
                    if (checkAll.isChecked){
                        likeCheck.isChecked = true
                        chatCheck.isChecked = true
                        waveCheck.isChecked = true
                    } else {
                        likeCheck.isChecked = false
                        chatCheck.isChecked = false
                        waveCheck.isChecked = false
                    }
                }
                R.id.sw_push_alarm_like, R.id.sw_push_alarm_chat, R.id.sw_push_alarm_wave -> {
                    checkAll.isChecked = likeCheck.isChecked && chatCheck.isChecked && waveCheck.isChecked
                }
            }
            Timber.d("isCheck\n all : ${checkAll.isChecked}\n" +
                    " like : ${likeCheck.isChecked}\n" +
                    " chat : ${chatCheck.isChecked}\n" +
                    " wave : ${waveCheck.isChecked} ")

            prefs.edit().putBoolean("checkAll", checkAll.isChecked).apply()
            prefs.edit().putBoolean("chatCheck", chatCheck.isChecked).apply()
            prefs.edit().putBoolean("likeCheck", likeCheck.isChecked).apply()
            prefs.edit().putBoolean("waveCheck", waveCheck.isChecked).apply()
        }
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav() { (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE }
}