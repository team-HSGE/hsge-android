package com.starters.hsge.presentation.main.detail

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentChatPartnerSpecificDogBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class SpecificDogFragment :
    BaseFragment<FragmentChatPartnerSpecificDogBinding>(R.layout.fragment_chat_partner_specific_dog) {

    private val args: SpecificDogFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dogDetail = args.partnerDogDetail

        setNavigation()
        setupToolbar()
        createTagTextView(binding.likeChipsContainer, args.partnerDogDetail.tag.tagLike)
        createTagTextView(binding.dislikeChipsContainer, args.partnerDogDetail.tag.tagDisLike)
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupToolbar() {
        //binding.tvPartnerSpecificDogTitle.text = requireArguments().getString("nickname")
    }

    private fun createTagTextView(container: LinearLayout, tagList: List<String>) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, 30, 0)
        for (i in tagList) {
            val textView = TextView(requireContext())
            textView.text = i
            textView.background = resources.getDrawable(R.drawable.bg_g200_r14, null)
            textView.setPadding(34, 22, 34, 22)
            container.addView(textView)
            textView.layoutParams = layoutParams
        }
    }
}