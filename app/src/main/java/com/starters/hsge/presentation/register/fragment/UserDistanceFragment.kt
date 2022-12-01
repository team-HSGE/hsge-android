package com.starters.hsge.presentation.register.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        binding.btnNext.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)

            // 다음 누르면 이 test_latitude를 멀티파트에 담아서 통신으로 보내면 됨
            val latitude = prefs.getString("latitude", "0").toString().toDouble()
            val longitude = prefs.getString("longitude", "0").toString().toDouble()
            Log.d("테스트_위도경도", "위도:${latitude}, 경도:${longitude} ")

            prefs.edit().remove("address").apply()
            prefs.edit().remove("longitude").apply()
            prefs.edit().remove("latitude").apply()

            //activity?.finish()  //RegisterActivity 종료
        }
    }
}