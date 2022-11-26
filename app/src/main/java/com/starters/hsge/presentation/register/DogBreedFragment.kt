package com.starters.hsge.presentation.register

import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogBreedBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.dialog.BottomSheetDialog

class DogBreedFragment : BaseFragment<FragmentDogBreedBinding>(R.layout.fragment_dog_breed) {

    private lateinit var ageBottomSheet: BottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dogBreedList = listOf<String>(
            "믹스", "리트리버", "닥스훈트", "말티즈", "슈나우저", "스탠다드 푸들", "토이푸들",
            "미니어처 핀셔", "배들링턴 테리어", "보더 콜리", "보스턴 테리어", "비글",
            "비숑 프리제", "사모예드", "시바 이누", "시베리안 허스키", "시츄", "코카 스파니엘",
            "요크셔테리어", "웰시 코기", "이탈리안 그레이하운드", "스피츠", "진돗개", "치와와",
            "파피용", "퍼그", "포메라니안", "프렌치 불독"
        )

        initListener(dogBreedList)
    }

    private fun initListener(list: List<String>) {
        binding.tvDogBreed.setOnClickListener {
            ageBottomSheet = BottomSheetDialog(list)
            ageBottomSheet.show(childFragmentManager, BottomSheetDialog.TAG)
        }
    }
}