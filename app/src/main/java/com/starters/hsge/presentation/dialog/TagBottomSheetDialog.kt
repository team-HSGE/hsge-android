package com.starters.hsge.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentTagBottomSheetDialogBinding
import com.starters.hsge.presentation.common.extension.showToast
import com.starters.hsge.presentation.main.mypage.edit.ViewType

class TagBottomSheetDialog(
    private val list: List<String>,
    private val type: ViewType,
    private val checkedList: List<String>,
    private val okBtnClickListener: (List<String>) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTagBottomSheetDialogBinding

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
                    } else if (ids.isEmpty()) {
                        binding.tvOkBtn.isEnabled = false
                        requireContext().showToast("태그를 하나 이상 선택해주세요")
                    } else {
                        binding.tvOkBtn.isEnabled = true
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
                    } else if (ids.isEmpty()) {
                        binding.tvOkBtn.isEnabled = false
                        requireContext().showToast("태그를 하나 이상 선택해주세요")
                    } else {
                        binding.tvOkBtn.isEnabled = true
                    }
                }
                return chip
            }
        }
    }

    private fun initListener() {
        binding.tvOkBtn.setOnClickListener {
            okBtnClickListener.invoke(getCheckedChipsText())
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

    private fun getCheckedChipsText(): MutableList<String> {
        val tagList: MutableList<String> = mutableListOf()
        for (index in 0 until binding.chipGroup.childCount) {
            val chip = binding.chipGroup.getChildAt(index) as Chip
            if (binding.chipGroup.checkedChipIds.contains(chip.id)) {
                tagList.add(chip.text.toString())
            }
        }
        return tagList
    }

    companion object {
        const val TAG = "TagBottomSheetDialog"
    }
}