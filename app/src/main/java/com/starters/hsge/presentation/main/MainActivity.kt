package com.starters.hsge.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivityMainBinding
import com.starters.hsge.presentation.common.base.BaseActivity
import com.starters.hsge.presentation.main.chatroom.ChatRoomFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
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
        moveFragment(moveTo)
    }

    private fun moveFragment(moveTo: String?) {
        val naviController =
            supportFragmentManager.findFragmentById(R.id.fcv_main)?.findNavController()
        naviController?.let { // null이 아닐 때만 확인하기 위해 let 사용
            binding.navigationMain.setupWithNavController(it)

            val navHostFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.fcv_main)
            val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)

            when (moveTo) {
                "chatFragment" -> {
                    if(currentFragment is ChatRoomFragment){ // 현재 화면이 chatRoomFragment -> 화면 이동 x
                        supportFragmentManager.popBackStack()
                        visibleBtmNav()
                    }else {
                        val item = binding.navigationMain.menu.findItem(R.id.chatFragment)
                        NavigationUI.onNavDestinationSelected(item, naviController)
                    }
                }
                "chatRoomFragment" -> {
                    val item = binding.navigationMain.menu.findItem(R.id.chatFragment)
                    NavigationUI.onNavDestinationSelected(item, naviController)

                    if(currentFragment is ChatRoomFragment){ // 현재 화면이 chatRoomFragment -> 화면 이동 x
                        return
                    }else {
                        findNavController(binding.fcvMain).navigate(R.id.action_chatFragment_to_chatRoomFragment)
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
}
