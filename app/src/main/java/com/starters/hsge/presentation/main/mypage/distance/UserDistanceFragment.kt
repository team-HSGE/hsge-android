package com.starters.hsge.presentation.main.mypage.distance

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserDistanceBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.MainActivity
import com.starters.hsge.data.interfaces.distanceInterface
import com.starters.hsge.data.service.DistanceService
import com.starters.hsge.presentation.common.util.LoadingDialog

class UserDistanceFragment : BaseFragment<FragmentUserDistanceBinding>(R.layout.fragment_user_distance), distanceInterface {

    private val args : UserDistanceFragmentArgs by navArgs()
    private lateinit var callback: OnBackPressedCallback
    private var distance = 3

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radius = args.locationInfo?.radius
        Log.d("사용자정보받아온거", "$radius")

        if (radius != null) {
            seekbar((radius*100).toInt())
        }
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

            DistanceService(this).tryPostDistance(distance.toDouble())
            LoadingDialog.showDogLoadingDialog(requireContext())
        }
    }

    // 화면 이동
    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav(){ (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE }

    // 반경 재설정_통신
    override fun onPostDistanceSuccess(isSuccess: Boolean, code: Int) {
        if(isSuccess){
            Log.d("Distance", "성공")
            LoadingDialog.dismissDogLoadingDialog()
            findNavController().navigate(R.id.action_userDistanceFragment_to_myPageFragment)
            (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
        }else{
            Log.d("Distance 오류", "Error code : ${code}")
            LoadingDialog.dismissDogLoadingDialog()
            showToast("잠시 후 다시 시도해주세요")
        }
    }

    override fun onPostIsLikeFailure(message: String) {
        Log.d("Distance 오류", "오류: $message")
        LoadingDialog.dismissDogLoadingDialog()
        showToast("잠시 후 다시 시도해주세요")
    }
}
