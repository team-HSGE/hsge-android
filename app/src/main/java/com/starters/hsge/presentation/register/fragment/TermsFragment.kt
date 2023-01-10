package com.starters.hsge.presentation.register.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.navigation.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentTermsBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsFragment : BaseFragment<FragmentTermsBinding>(R.layout.fragment_terms) {

    private var url = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        changeTextColor()
        checkTerms()
    }

    private fun initListener(){

        binding.ivFirstTerm.setOnClickListener {
            url = "https://sites.google.com/view/apphsge/%EC%84%9C%EB%B9%84%EC%8A%A4-%EC%9D%B4%EC%9A%A9%EC%95%BD%EA%B4%80?authuser=0"
            setNavAction(it, url)
        }

        binding.ivSecondTerm.setOnClickListener {
            url = "https://sites.google.com/view/apphsge/%EA%B0%9C%EC%9D%B8%EC%A0%95%EB%B3%B4-%EC%B2%98%EB%A6%AC%EB%B0%A9%EC%B9%A8?authuser=0"
            setNavAction(it, url)
        }

        binding.ivThirdTerm.setOnClickListener {
            url = "https://sites.google.com/view/apphsge/%EC%9C%84%EC%B9%98%EA%B8%B0%EB%B0%98%EC%84%9C%EB%B9%84%EC%8A%A4-%EC%9D%B4%EC%9A%A9%EC%95%BD%EA%B4%80?authuser=0"
            setNavAction(it, url)
        }
    }

    private fun setNavAction(view: View, termUrl: String){
        val action = TermsFragmentDirections.actionTermsFragmentToTermWebViewFragment(termUrl)
        view.findNavController().navigate(action)
    }

    private fun changeTextColor() {
        val textData: String = binding.termsGuideSecondLine.text.toString()
        val builder = SpannableStringBuilder(textData)

        // 색깔 적용
        val colorBlueSpan = ForegroundColorSpan(Color.parseColor("#FFBF43"))
        builder.setSpan(colorBlueSpan, 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.termsGuideSecondLine.text = builder
    }

    private fun checkTerms(){
        binding.cbTotalAgree.setOnClickListener(SwitchListener())
        binding.cbFirstTerm.setOnClickListener(SwitchListener())
        binding.cbSecondTerm.setOnClickListener(SwitchListener())
        binding.cbThirdTerm.setOnClickListener(SwitchListener())

    }

    inner class SwitchListener : View.OnClickListener {
        override fun onClick(v: View?) {

            val totalCheck = binding.cbTotalAgree
            val firstCheck = binding.cbFirstTerm
            val secondCheck = binding.cbSecondTerm
            val thirdCheck = binding.cbThirdTerm

            when (v?.id) {
                R.id.cb_total_agree -> {
                    if (totalCheck.isChecked) {
                        firstCheck.isChecked = true
                        secondCheck.isChecked = true
                        thirdCheck.isChecked = true
                    } else {
                        firstCheck.isChecked = false
                        secondCheck.isChecked = false
                        thirdCheck.isChecked = false
                    }
                }
                R.id.cb_first_term, R.id.cb_second_term, R.id.cb_third_term -> {
                    totalCheck.isChecked = firstCheck.isChecked && secondCheck.isChecked && thirdCheck.isChecked
                }
            }
            changeAgreeBtn()
        }
    }

    private fun changeAgreeBtn(){
            binding.btnNext.isEnabled = binding.cbTotalAgree.isChecked
    }
}