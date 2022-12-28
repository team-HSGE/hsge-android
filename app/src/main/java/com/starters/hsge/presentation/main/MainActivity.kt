package com.starters.hsge.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivityMainBinding
import com.starters.hsge.presentation.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigation()

        //인텐트가 잇으면 sp 확인해서 넘어가기
        val notificationPayload = intent?.extras
        if (notificationPayload != null) {
            CoroutineScope(Dispatchers.Main).launch {

                delay(1000L) // DogCardResponse 올 때까지 1초 시간 벌기

                if (prefs.getString("homeDogResponse", "0").toString() == "완료") {
                    Log.d("순서", "${prefs.getString("homeDogResponse", "0").toString()}")
                    val moveTo = notificationPayload.getString("pushAbout")
                    Log.d("순서?", "ㅇㅋ moveFragment 들어가")

                    moveFragment(moveTo, "killed")

                    prefs.edit().remove("homeDogResponse").apply()

                }


            }
        }
    }

    private fun initNavigation() {
        val naviController =
            supportFragmentManager.findFragmentById(R.id.fcv_main)?.findNavController()
        naviController?.let {
            binding.navigationMain.setupWithNavController(it)


        }
    }

    // background_앱이 꺼져있는 경우 push
    private fun killedPush() {


    }

    // foreground, background_화면 안 떠있는 경우 화면 이동
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val moveTo = intent?.extras!!.getString("pushAbout")
        moveFragment(moveTo, "fore")
    }


    private fun moveFragment(moveTo: String?, app: String) {
        val naviController =
            supportFragmentManager.findFragmentById(R.id.fcv_main)?.findNavController()
        naviController?.let { // null이 아닐 때만 확인하기 위해 let 사용
            binding.navigationMain.setupWithNavController(it)

            when (moveTo) {
                "chatFragment" -> {
                    val item = binding.navigationMain.menu.findItem(R.id.chatFragment)
                    NavigationUI.onNavDestinationSelected(item, naviController)
                }
                "chatRoomFragment" -> {
                    val item = binding.navigationMain.menu.findItem(R.id.chatFragment)
                    Log.d("순서?", "chatFrag")
                    NavigationUI.onNavDestinationSelected(item, naviController)

                    if (prefs.getString("chatListResponse", "0").toString() == "완료") {
                        findNavController(binding.fcvMain).navigate(R.id.action_chatFragment_to_chatRoomFragment)
                        Log.d("순서?", "charRoomFrag")

                        goneBtmNav()
                        prefs.edit().remove("chatListResponse").apply()
                    } else {
                        return
                    }
                }
                else -> return
            }
        }
    }

    private fun goneBtmNav() {
        binding.navigationMain.visibility = View.GONE
    }
}
