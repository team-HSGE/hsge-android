package com.starters.hsge.presentation.main

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivityMainBinding
import com.starters.hsge.presentation.common.base.BaseActivity

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
}