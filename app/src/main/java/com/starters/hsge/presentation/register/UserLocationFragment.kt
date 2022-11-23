package com.starters.hsge.presentation.register

import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserLocationBinding
import com.starters.hsge.presentation.common.base.BaseFragment

class UserLocationFragment : BaseFragment<FragmentUserLocationBinding>(R.layout.fragment_user_location) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 위치권한 싸기
    }
}