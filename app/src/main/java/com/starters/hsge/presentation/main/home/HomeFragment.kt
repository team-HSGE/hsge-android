package com.starters.hsge.presentation.main.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.starters.hsge.R
import com.starters.hsge.data.model.DogCard
import com.starters.hsge.databinding.FragmentHomeBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.home.adapter.CardStackAdapter
import com.starters.hsge.presentation.main.home.network.RetrofitApi
import com.yuyakaido.android.cardstackview.*
import retrofit2.Response

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofitWork()

        manager = CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }

        })

//        val dogCardList = listOf<DogCard>(
//            DogCard(
//                "뽀삐",
//                "https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg",
//                "스탠다드 푸들",
//                " I 12살 10개월 I ",
//                "Female",
//                true,
//                Tag(listOf("고구마", "간식", "수영"), listOf("산책", "카메라"))
//            )
//        )



        fabClick(binding.cardStackView)
    }

    private fun fabClick(cardStackView: CardStackView) {
        clickDislike(cardStackView)
        clickLike(cardStackView)
    }

    private fun clickLike(cardStackView: CardStackView) {
        binding.fabLike.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }
    }

    private fun clickDislike(cardStackView: CardStackView) {
        binding.fabDislike.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }
    }

    private fun retrofitWork(){
        RetrofitApi.dogService.getDogData().enqueue(object : retrofit2.Callback<List<DogCard>>{
            override fun onResponse(call: retrofit2.Call<List<DogCard>>, response: Response<List<DogCard>>) {
                if (response.isSuccessful) {
                    Log.d("TAG", response.toString())
                    val dogCardResult = response.body()
                    cardStackAdapter = CardStackAdapter(requireContext(), dogCardResult!!)
                    manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
                    binding.cardStackView.layoutManager = manager
                    binding.cardStackView.adapter = cardStackAdapter
                } else{
                    Log.d("TAG", "성공")
                }

            }

            override fun onFailure(call: retrofit2.Call<List<DogCard>>, t: Throwable) {
                Log.d("TAG", "실패")
                Log.d("TAG", t.toString())
            }
        })
    }
}