package com.starters.hsge.presentation.main.mypage.userDistance

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.data.model.remote.request.DistanceRequest
import com.starters.hsge.databinding.FragmentUserDistanceBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.presentation.main.home.network.RetrofitApi
import com.starters.hsge.presentation.main.mypage.userDistance.network.DistanceService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDistanceFragment : BaseFragment<FragmentUserDistanceBinding>(R.layout.fragment_user_distance) {

    private lateinit var callback: OnBackPressedCallback
    private var distance = 3

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO : 마이페이지에서 서버에 저장된 radius 넘어옴. 이 radius에 넣으면 됨
        val radius = 14

        seekbar(radius)
        setNavigation()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
                visibleBtmNav()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }


    // seekBar - 반경 설정
    private fun seekbar(radius: Int) {
        // 이전에 정했던 반경으로 디폴트 값
        binding.userDistanceSeekbar.setProgress(radius-3)
        binding.tvLocationDistance.text = "${radius}km"

        val max = 15
        val min = 3
        binding.userDistanceSeekbar.max = max - min

        binding.userDistanceSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                distance = progress + min
                binding.tvLocationDistance.text = "${distance}km"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })


        // 완료 버튼
        binding.btnNext.setOnClickListener {
            // post로 api 통신하면 됨
            findNavController().navigate(R.id.action_userDistanceFragment_to_homeFragment)
            (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE

            retrofitWork(distance.toDouble())
        }
    }


    // 화면 이동
    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }


    // 반경 재설정_통신
    private fun retrofitWork(distance: Double){
        val distanceRetrofit = RetrofitApi.retrofit.create(DistanceService::class.java)
        val radius = distance / 100

        distanceRetrofit.putDistanceData(request = DistanceRequest(radius)).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("distance", radius.toString())
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