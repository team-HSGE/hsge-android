package com.starters.hsge.presentation.onboarding

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.starters.hsge.App
import com.starters.hsge.R
import com.starters.hsge.databinding.ActivityOnboardingBinding
import com.starters.hsge.presentation.common.base.BaseActivity
import com.starters.hsge.presentation.common.constants.STATUS_ONBOARDING_OUT

class OnBoardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding){

    private val mPageNumbers = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // adapter 세팅
        val pagerAdapter = ViewPagerAdapter(this ,getListOfPagerContents(),mPageNumbers)
        binding.vpContents.adapter = pagerAdapter

        // indicator 세팅
        TabLayoutMediator(binding.viewpagerIndicator, binding.vpContents) { tab, position -> }.attach()

        // 온보딩 중간에 나간 경우 체킹
        App.prefs.edit().putBoolean(STATUS_ONBOARDING_OUT, true).apply()
    }

    private fun getListOfPagerContents(): List<Array<String>> {
        val ar1 = arrayOf(getString(R.string.onboarding_title_1), getString(R.string.onboarding_subtitle_1),"ic_onboarding_1", "1" )
        val ar2 = arrayOf(getString(R.string.onboarding_title_2), getString(R.string.onboarding_subtitle_2) ,"ic_onboarding_2", "2")
        val ar3 = arrayOf(getString(R.string.onboarding_title_3), getString(R.string.onboarding_subtitle_3) ,"ic_onboarding_3", "3")
        val ar4 = arrayOf(getString(R.string.onboarding_title_4), getString(R.string.onboarding_subtitle_4) ,"ic_onboarding_4", "4")
        return listOf(ar1, ar2, ar3, ar4)
    }
}