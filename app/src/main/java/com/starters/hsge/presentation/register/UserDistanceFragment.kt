package com.starters.hsge.presentation.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserDistanceBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity

class UserDistanceFragment : BaseFragment<FragmentUserDistanceBinding>(R.layout.fragment_user_distance) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()

    }

    private fun initListener() {
        binding.tvNextButton.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            //activity?.finish()    //RegisterActivity 종료
        }
    }

}