package com.starters.hsge.presentation.main.chat.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
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
        binding.tvPartnerSpecificDogTitle.text = args.partnerNickName

        setNavigation()
        createLikeTagTextView(binding.likeChipsContainer, args.partnerDogDetail.tag.tagLike)
        createDislikeTagTextView(
            binding.dislikeChipsContainer,
            args.partnerDogDetail.tag.tagDisLike
        )
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun createLikeTagTextView(container: LinearLayout, tagList: List<String>) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, 30, 0)
        for (i in tagList) {
            val textView = TextView(requireContext())
            textView.text = i
            textView.setTextColor(Color.parseColor("#222222"))
            context?.resources?.let {
                textView.compoundDrawablePadding = it.getDimensionPixelSize(R.dimen.textIcon)
            }
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_chip_like,
                0,
                0,
                0
            )
            textView.background =
                ResourcesCompat.getDrawable(resources, R.drawable.bg_light_yellow_r16, null)
            textView.setPadding(34, 18, 34, 18)
            container.addView(textView)
            textView.layoutParams = layoutParams
        }
    }

    private fun createDislikeTagTextView(container: LinearLayout, tagList: List<String>) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, 30, 0)
        for (i in tagList) {
            val textView = TextView(requireContext())
            textView.text = i
            textView.setTextColor(Color.parseColor("#222222"))
            context?.resources?.let {
                textView.compoundDrawablePadding = it.getDimensionPixelSize(R.dimen.textIcon)
            }
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_chip_dislike,
                0,
                0,
                0
            )
            textView.background =
                ResourcesCompat.getDrawable(resources, R.drawable.bg_g200_r16, null)
            textView.setPadding(34, 18, 34, 18)
            container.addView(textView)
            textView.layoutParams = layoutParams
        }
    }
}