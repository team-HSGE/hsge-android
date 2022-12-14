package com.starters.hsge.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentTagBottomSheetDialogBinding
import com.starters.hsge.presentation.main.edit.ViewType

class TagBottomSheetDialog(
    private val list: List<String>,
    private val type: ViewType,
    private val checkedList: List<String>
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTagBottomSheetDialogBinding
//    private lateinit var mContentClickListener: BottomSheetClickListener

//    interface BottomSheetClickListener {
//        fun onContentClick(content: String)
//    }
//
//    fun setBottomSheetClickListener(btnClickListener: BottomSheetClickListener) {
//        mContentClickListener = btnClickListener
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tag_bottom_sheet_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setUpChipGroupDynamically(list, type)
        updateCheckedChips(checkedList)
        initListener()
    }

    private fun setUpChipGroupDynamically(chipList: List<String>, type: ViewType) {
        for (i in chipList) {
            binding.chipGroup.addView(createChip(i, type))
        }
    }

    private fun createChip(label: String, type: ViewType): Chip {
        when (type) {
            ViewType.LIKE -> {

                val chip = Chip(context, null, R.attr.CustomLikeChipChoiceStyle)
                chip.text = label

                chip.setOnClickListener {
                    val ids: List<Int> = binding.chipGroup.checkedChipIds
                    if (ids.size > 3) {
                        chip.isChecked = false
                    }

                }
                return chip
            }
            ViewType.DISLIKE -> {
                val chip = Chip(context, null, R.attr.CustomDislikeChipChoiceStyle)
                chip.text = label

                chip.setOnClickListener {
                    val ids: List<Int> = binding.chipGroup.checkedChipIds
                    if (ids.size > 3) {
                        chip.isChecked = false
                    }

                }
                return chip
            }
        }
    }

    private fun initListener() {
        binding.tvOkBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun updateCheckedChips(tagList: List<String>) {
        for (index in 0 until binding.chipGroup.childCount) {
            val chip = binding.chipGroup.getChildAt(index) as Chip
            if (tagList.contains(chip.text)) {
                chip.isChecked = true
            }
        }
    }

    companion object {
        const val TAG = "TagBottomSheetDialog"
    }
}