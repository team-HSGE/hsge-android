package com.starters.hsge.presentation.register

import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogAgeBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog

class DogAgeFragment : BaseFragment<FragmentDogAgeBinding>(R.layout.fragment_dog_age) {

    private lateinit var ageBottomSheet: BottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dogAgeList = listOf<String>(
            "1개월 미만", "1개월", "2개월", "3개월", "4개월", "5개월",
            "6개월", "7개월", "8개월", "9개월", "10개월", "11개월",
            "1살", "2살", "3살", "4살", "5살", "6살", "7살", "8살",
            "9살", "10살", "11살", "12살", "13살", "14살", "15살", "16살",
            "17살", "18살", "19살", "20살", "20살 이상"
        )

        initListener(dogAgeList)
    }

    private fun initListener(list: List<String>) {
        binding.tvDogAge.setOnClickListener {
            ageBottomSheet = BottomSheetDialog(list)
            ageBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
        }
    }
}