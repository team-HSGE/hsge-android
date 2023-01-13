package com.starters.hsge.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.starters.hsge.presentation.common.constants.ONBOARDING_STRING_OBJECT

class ViewPagerAdapter(fragment: Fragment, val listOfPagerContents: List<Array<String>>, val mPageNumbers :Int)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = mPageNumbers

    override fun createFragment(position: Int): Fragment {
        val fragment = ViewPagerContentFragment()

        when(position) {
            0 -> {
                fragment.arguments = Bundle().apply {
                    putStringArray(ONBOARDING_STRING_OBJECT, listOfPagerContents[0])
                }
            }
            1 -> {
                fragment.arguments = Bundle().apply {
                    putStringArray(ONBOARDING_STRING_OBJECT, listOfPagerContents[1])
                }
            }
            2 -> {
                fragment.arguments = Bundle().apply {
                    putStringArray(ONBOARDING_STRING_OBJECT, listOfPagerContents[2])
                }
            }
            3 -> {
                fragment.arguments = Bundle().apply {
                    putStringArray(ONBOARDING_STRING_OBJECT, listOfPagerContents[3])
                }
            }
        }
        return fragment
    }

}