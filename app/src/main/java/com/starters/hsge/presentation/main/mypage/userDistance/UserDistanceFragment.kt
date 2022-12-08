package com.starters.hsge.presentation.main.mypage.userDistance

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.data.model.Distance
import com.starters.hsge.databinding.FragmentUserDistanceBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.home.network.RetrofitApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDistanceFragment :
    BaseFragment<FragmentUserDistanceBinding>(R.layout.fragment_user_distance) {

    val distance = prefs.getInt("distance", 3)
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

            retrofitWork(distance.toDouble())
        }
    }

    private fun seekbar() {
        // 이전에 정했던 반경으로 디폴트 값
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

    private fun retrofitWork(distance: Double){
        val distanceRetrofit = RetrofitApi.retrofit.create(DistanceService::class.java)
        val radius = distance / 100

        distanceRetrofit.postDistanceData(request = Distance(radius)).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("distance", response.toString())
                    Log.d("distance", "성공")
                } else{
                    Log.d("distance", "실패")
                    Log.d("distance", response.code().toString())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("distance", "실패")
                Log.d("distance", t.toString())
            }
        })
    }
}