package com.starters.hsge.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentOnboardingcontentBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.constants.ONBOARDING_STRING_OBJECT
import com.starters.hsge.presentation.main.MainActivity


class ViewPagerContentFragment : BaseFragment<FragmentOnboardingcontentBinding>(R.layout.fragment_onboardingcontent) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(ONBOARDING_STRING_OBJECT) }?.apply {
            binding.tvOnboardingTitle.text = getStringArray(ONBOARDING_STRING_OBJECT)!![0]
            binding.tvOnboardingSubtitle.text = getStringArray(ONBOARDING_STRING_OBJECT)!![1]
            val img = getStringArray(ONBOARDING_STRING_OBJECT)!![2]
            binding.ivOnboardingImg.setImageResource(resources.getIdentifier(img, "drawable", context?.packageName))
            val tab = getStringArray(ONBOARDING_STRING_OBJECT)!![3].toInt()
            setStartBtn(tab)
        }

        binding.onboardingStartBtn.setOnClickListener {
            moveToMain()
        }
    }

    private fun setStartBtn(tab : Int) {
        if (tab == 4) {
            binding.onboardingStartBtn.visibility = View.VISIBLE
            binding.ivOnboardingImg.setPadding(0, 0, 0, 0)
        } else {
            binding.onboardingStartBtn.visibility = View.GONE
        }
    }

    private fun moveToMain() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        activity?.finish()
    }
}