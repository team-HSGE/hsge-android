package com.starters.hsge.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivityMainBinding
import com.starters.hsge.presentation.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigation()
        killedPush()
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
        val notificationPayload = intent?.extras
        if (notificationPayload != null) {

            val moveTo = notificationPayload.getString("pushAbout")
            moveFragment(moveTo)
        }
    }

    // foreground, background_화면 안 떠있는 경우 화면 이동
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val moveTo = intent?.extras!!.getString("pushAbout")
        moveFragment(moveTo)
    }


    private fun moveFragment(moveTo: String?) {
        val naviController = supportFragmentManager.findFragmentById(R.id.fcv_main)?.findNavController()
        when (moveTo) {
            "chatFragment" -> {
                val item = binding.navigationMain.menu.findItem(R.id.chatFragment)
                NavigationUI.onNavDestinationSelected(item, naviController!!)
            }
            "chatRoomFragment" -> {
                naviController!!.navigate(R.id.chatFragment)
                naviController.navigate(R.id.chatRoomFragment)
                goneBtmNav()
            }
            else -> return
        }
    }

    private fun goneBtmNav(){ binding.navigationMain.visibility = View.GONE }
}