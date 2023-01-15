package com.starters.hsge.presentation.main.mypage.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.user.UserApiClient
import com.starters.hsge.R
import com.starters.hsge.data.interfaces.SettingsInterface
import com.starters.hsge.data.model.remote.request.FcmPostRequest
import com.starters.hsge.data.service.SettingsService
import com.starters.hsge.databinding.FragmentSettingsBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.ConfirmDialogFragment
import com.starters.hsge.presentation.login.LoginActivity
import com.starters.hsge.presentation.main.MainActivity
import timber.log.Timber

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(R.layout.fragment_settings),
    SettingsInterface {

    private lateinit var callback: OnBackPressedCallback
    private var appAlarm = true
    private var url = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigation()
        initListener()
        navigateWithUrl()
    }

    override fun onResume() {
        super.onResume()
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

    private fun initListener() {
        binding.settingWithdrawalSection.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_withdrawalFragment)
        }

        binding.settingLogoutSection.setOnClickListener {
            val dialog = ConfirmDialogFragment("로그아웃 하시겠습니까?")

            dialog.setButtonClickListener(object : ConfirmDialogFragment.OnButtonClickListener {
                override fun onCancelBtnClicked() {
                    // 취소 버튼 클릭했을 때 처리
                }

                override fun onOkBtnClicked() {
                    // 확인 버튼 클릭했을 때 처리
                    UserApiClient.instance.logout { error ->
                        if (error != null) {
                            Log.d("로그아웃", "로그아웃 실패 : ${error}")
                            showToast("다시 시도해주세요.")
                        } else {
                            // access token & fcm token 날리기
                            val fcmToken = FcmPostRequest(prefs.getString("fcmToken", ""))
                            Log.d("확인", "$fcmToken")

                            SettingsService(this@SettingsFragment).tryDeleteFcmToken(token = fcmToken)
                        }
                    }
                }
            })
            dialog.show(childFragmentManager, "CustomDialog")
        }
    }

    private fun checkNotification() {
        appAlarm = NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()
        val spAll = prefs.getBoolean("checkAll", true)
        val spChat = prefs.getBoolean("chatCheck", true)
        val spLike = prefs.getBoolean("likeCheck", true)
        val spWave = prefs.getBoolean("waveCheck", true)

        if (appAlarm) {
            when {
                spAll -> {
                    changeCheckSub(true)
                    changeCheckAll(true)
                }
                !spLike || !spWave || !spChat -> {
                    changeCheckAll(false)
                }
            }

            if (spLike) {
                binding.swPushAlarmLike.isChecked = true
            }
            if (spChat) {
                binding.swPushAlarmChat.isChecked = true
            }
            if (spWave) {
                binding.swPushAlarmWave.isChecked = true
            }
        } else {
            changeCheckSub(false)
            changeCheckAll(false)
        }

        binding.swPushAlarmAll.setOnClickListener(SwitchListener())
        binding.swPushAlarmLike.setOnClickListener(SwitchListener())
        binding.swPushAlarmChat.setOnClickListener(SwitchListener())
        binding.swPushAlarmWave.setOnClickListener(SwitchListener())
    }

    inner class SwitchListener : View.OnClickListener {
        override fun onClick(v: View?) {

            val likeCheck = binding.swPushAlarmLike
            val waveCheck = binding.swPushAlarmWave
            val chatCheck = binding.swPushAlarmChat
            val checkAll = binding.swPushAlarmAll

            when (v?.id) {
                R.id.sw_push_alarm_all -> {
                    if (checkAll.isChecked) {
                        if (!appAlarm) {
                            showPermissionDialog()
                        } else {
                            changeCheckSub(true)
                        }
                    } else {
                        changeCheckSub(false)
                    }
                }
                R.id.sw_push_alarm_like, R.id.sw_push_alarm_chat, R.id.sw_push_alarm_wave -> {
                    if (!appAlarm) {
                        showPermissionDialog()
                    } else {
                        checkAll.isChecked =
                            likeCheck.isChecked && chatCheck.isChecked && waveCheck.isChecked
                    }
                }
            }
            Timber.d(
                "isCheck\n all : ${checkAll.isChecked}\n" +
                        " like : ${likeCheck.isChecked}\n" +
                        " chat : ${chatCheck.isChecked}\n" +
                        " wave : ${waveCheck.isChecked} "
            )

            prefs.edit().putBoolean("checkAll", checkAll.isChecked).apply()
            prefs.edit().putBoolean("chatCheck", chatCheck.isChecked).apply()
            prefs.edit().putBoolean("likeCheck", likeCheck.isChecked).apply()
            prefs.edit().putBoolean("waveCheck", waveCheck.isChecked).apply()
        }
    }

    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("설정에서 알림 권한을 허용해주세요")
        builder.setPositiveButton("설정으로 이동하기") { _, _ ->
            val intent = Intent().apply {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, activity?.packageName)
                }
            startActivity(intent)
        }
        builder.show()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav() {
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }

    private fun changeCheckAll(isChecked: Boolean) {
        binding.swPushAlarmAll.isChecked = isChecked
    }

    private fun changeCheckSub(isChecked: Boolean) {
        binding.swPushAlarmLike.isChecked = isChecked
        binding.swPushAlarmChat.isChecked = isChecked
        binding.swPushAlarmWave.isChecked = isChecked
    }

    private fun navigateWithUrl(){
        binding.settingServiceTermsFirstSection.setOnClickListener {
            url = "https://sites.google.com/view/apphsge/%EC%84%9C%EB%B9%84%EC%8A%A4-%EC%9D%B4%EC%9A%A9%EC%95%BD%EA%B4%80?authuser=0"
            setNavAction(it, url)
        }

        binding.settingServiceTermsSecondSection.setOnClickListener {
            url = "https://sites.google.com/view/apphsge/%EA%B0%9C%EC%9D%B8%EC%A0%95%EB%B3%B4-%EC%B2%98%EB%A6%AC%EB%B0%A9%EC%B9%A8?authuser=0"
            setNavAction(it, url)
        }

        binding.settingServiceTermsThirdSection.setOnClickListener {
            url = "https://sites.google.com/view/apphsge/%EC%9C%84%EC%B9%98%EA%B8%B0%EB%B0%98%EC%84%9C%EB%B9%84%EC%8A%A4-%EC%9D%B4%EC%9A%A9%EC%95%BD%EA%B4%80?authuser=0"
            setNavAction(it, url)
        }
    }

    private fun setNavAction(view: View, termUrl: String){
        val action = SettingsFragmentDirections.actionSettingsFragmentToTermWebViewFragment2(termUrl)
        view.findNavController().navigate(action)
    }
}