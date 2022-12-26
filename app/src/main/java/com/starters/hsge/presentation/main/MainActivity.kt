package com.starters.hsge.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
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

        // 백그라운드 푸시 화면 이동
        val notificationPayload = intent?.extras
        if(notificationPayload != null){
            val naviController =
                supportFragmentManager.findFragmentById(R.id.fcv_main)?.findNavController()
            val item = binding.navigationMain.menu.findItem(R.id.chatFragment)
            NavigationUI.onNavDestinationSelected(item, naviController!!)
        }
        Log.d("notificationPayload", notificationPayload.toString())

    }

    private fun initNavigation() {
        val naviController =
            supportFragmentManager.findFragmentById(R.id.fcv_main)?.findNavController()
        naviController?.let {
            binding.navigationMain.setupWithNavController(it)
        }
    }

    // 포그라운드 화면 이동
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Log.d("뭐가", "dd")

        val createdAt = intent?.extras!!.getString("pushAbout")
        Log.d("pushAbout", intent.extras!!.getString("pushAbout").toString())

        when (createdAt) {
            "chatFragment" -> {
                val item = binding.navigationMain.menu.findItem(R.id.chatFragment)
                NavigationUI.onNavDestinationSelected(
                    item,
                    navController = findNavController(R.id.fcv_main)
                )
            }
            "myPageFragment" -> {
                val item = binding.navigationMain.menu.findItem(R.id.myPageFragment)
                NavigationUI.onNavDestinationSelected(
                    item,
                    navController = findNavController(R.id.fcv_main)
                )
            }
            else -> return
        }


    }
}