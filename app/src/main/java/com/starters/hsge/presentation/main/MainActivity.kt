package com.starters.hsge.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.starters.hsge.R
import com.starters.hsge.common.constants.orderToIcon
import com.starters.hsge.databinding.ActivityMainBinding
import com.starters.hsge.domain.model.ChatListInfo
import com.starters.hsge.presentation.common.base.BaseActivity
import com.starters.hsge.presentation.main.chat.chat.ChatFragmentDirections
import com.starters.hsge.presentation.main.chat.chatroom.ChatRoomFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigation()
    }

    private fun initNavigation() {
        val naviController =
            supportFragmentManager.findFragmentById(R.id.fcv_main)?.findNavController()
        naviController?.let {
            binding.navigationMain.setupWithNavController(it)
        }
    }

    // foreground, background_화면 안 떠있는 경우 화면 이동
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val moveTo = intent?.extras!!.getString("pushAbout")
        val roomId = intent.extras!!.getLong("roomId")
        val nickname = intent.extras!!.getString("nickname")
        val userIcon = intent.extras!!.getInt("userIcon")
        moveFragment(moveTo, roomId, nickname, userIcon)
    }

    private fun moveFragment(moveTo: String?, roomId: Long?, nickname: String?, userIcon: Int) {
        val naviController =
            supportFragmentManager.findFragmentById(R.id.fcv_main)?.findNavController()
        naviController?.let { // null이 아닐 때만 확인하기 위해 let 사용
            binding.navigationMain.setupWithNavController(it)

            val navHostFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.fcv_main)
            val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)

            when (moveTo) {
                "chatFragment" -> {
                    if (currentFragment is ChatRoomFragment) { // 현재 화면이 chatRoomFragment -> 화면 이동 x
                        supportFragmentManager.popBackStack()
                        visibleBtmNav()
                    } else {
                        if (binding.navigationMain.selectedItemId == R.id.myPageFragment) {
                            naviController.popBackStack(R.id.myPageFragment, false)
                            visibleBtmNav()
                            binding.navigationMain.selectedItemId = R.id.chatFragment
                        } else {
                            binding.navigationMain.selectedItemId = R.id.chatFragment
                        }
                    }
                }
                "chatRoomFragment" -> {
                    val navHostFragment: Fragment? =
                        supportFragmentManager.findFragmentById(R.id.fcv_main)
                    var currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)

                    if (currentFragment is ChatRoomFragment) { // 현재 화면이 chatRoomFragment
                        binding.navigationMain.selectedItemId = R.id.chatFragment

                        val currentRoomId = prefs.getString("roomId", null).toString()
                        Log.d("현재 룸 아이디", currentRoomId)

                        if (currentRoomId == roomId.toString()) { // 룸아이디 같음 -> 화면 이동 x (사실 이 경우 푸시가 안 오게 함)
                            Log.d("현재 룸 == 푸시 룸", "같아")
                            return
                        } else { // 룸아이디 다름 -> 화면 이동
                            Log.d("현재 룸 == 푸시 룸", "달라")
                            naviController.popBackStack()
                            val action =
                                ChatFragmentDirections.actionChatFragmentToChatRoomFragment(
                                    chatInfo = ChatListInfo(
                                        roomId!!,
                                        nickname!!,
                                        userIcon.orderToIcon(),
                                        DEFAULT_MESSAGE,
                                        DEFAULT_CHECKED,
                                        DEFAULT_ACTIVE,
                                        DEFAULT_FIRST_DATE,
                                        DEFAULT_LAST_DATE
                                    )
                                )
                            findNavController(binding.fcvMain).navigate(action)
                        }
                        return
                    } else {
                        if (binding.navigationMain.selectedItemId == R.id.myPageFragment) {
                            naviController.popBackStack(R.id.myPageFragment, false)
                            visibleBtmNav()
                            binding.navigationMain.selectedItemId = R.id.chatFragment
                        } else {
                            binding.navigationMain.selectedItemId = R.id.chatFragment
                        }

                        val action =
                            ChatFragmentDirections.actionChatFragmentToChatRoomFragment(
                                chatInfo = ChatListInfo(
                                    roomId!!,
                                    nickname!!,
                                    userIcon.orderToIcon(),
                                    DEFAULT_MESSAGE,
                                    DEFAULT_CHECKED,
                                    DEFAULT_ACTIVE,
                                    DEFAULT_FIRST_DATE,
                                    DEFAULT_LAST_DATE
                                )
                            )
                        findNavController(binding.fcvMain).navigate(action)
                    }
                    goneBtmNav()
                }
                else -> return
            }
        }
    }

    private fun goneBtmNav() {
        binding.navigationMain.visibility = View.GONE
    }

    private fun visibleBtmNav() {
        binding.navigationMain.visibility = View.VISIBLE
    }

    companion object {
        const val DEFAULT_MESSAGE = "안녕하세요"
        const val DEFAULT_CHECKED = false
        const val DEFAULT_ACTIVE = true
        const val DEFAULT_FIRST_DATE = "2023.01.10"
        const val DEFAULT_LAST_DATE = "2023.01.13"
    }
}
