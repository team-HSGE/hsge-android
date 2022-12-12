package com.starters.hsge.presentation.main.mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserDistanceBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity

class UserDistanceFragment :
    BaseFragment<FragmentUserDistanceBinding>(R.layout.fragment_user_distance) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        seekbar()
        setNavigation()

    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            // post로 api 통신하면 됨
            findNavController().navigate(R.id.action_userDistanceFragment_to_homeFragment)
            (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
        }
    }

    private fun seekbar() {
        // 이전에 정했던 반경으로 디폴트 값
        val distance = prefs.getInt("distance", 3)
        binding.userDistanceSeekbar.setProgress(distance-3)
        binding.tvLocationDistance.text = "${distance}km"

        val max = 15
        val min = 3
        binding.userDistanceSeekbar.max = max - min

        binding.userDistanceSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                val distance = progress + min
                binding.tvLocationDistance.text = "${distance}km"
                Log.d("distance", distance.toString())
                prefs.edit().putInt("distance", distance).apply()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}