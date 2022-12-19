package com.starters.hsge.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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


    }

    private fun initNavigation() {
        val naviController =
            supportFragmentManager.findFragmentById(R.id.fcv_main)?.findNavController()
        naviController?.let {
            binding.navigationMain.setupWithNavController(it)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val createdAt = intent?.extras!!.getString("createdAt")
        Log.d("createdAt", createdAt.toString())

        when(createdAt){
            "chatFragment" ->{
                val item = binding.navigationMain.menu.findItem(R.id.chatFragment)
                NavigationUI.onNavDestinationSelected(item, navController = findNavController(R.id.fcv_main))
            }
        }
        visibleBtmNav()
    }

    private fun visibleBtmNav(){
       binding.navigationMain.visibility = View.VISIBLE
    }
}