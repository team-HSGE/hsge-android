package com.starters.hsge.presentation.register.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentTermsBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsFragment : BaseFragment<FragmentTermsBinding>(R.layout.fragment_terms) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeTextColor()
    }

    private fun changeTextColor() {
        val textData: String = binding.termsGuideSecondLine.text.toString()
        val builder = SpannableStringBuilder(textData)

        // 색깔 적용
        val colorBlueSpan = ForegroundColorSpan(Color.parseColor("#FFBF43"))
        builder.setSpan(colorBlueSpan, 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.termsGuideSecondLine.text = builder
    }
}