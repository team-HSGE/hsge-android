package com.starters.hsge.presentation.onboarding

import android.os.Bundle
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivityOnboardingBinding
import com.starters.hsge.presentation.common.base.BaseActivity

class OnBoardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding){

    val onboardingFragment = OnBoardingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().
                replace(binding.frm.id, onboardingFragment).commit()
    }
}