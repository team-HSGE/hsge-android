package com.starters.hsge.presentation.main.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.starters.hsge.R
import com.starters.hsge.data.model.DogCard
import com.starters.hsge.data.model.Tag
import com.starters.hsge.databinding.FragmentHomeBinding
import com.starters.hsge.network.RetrofitClient
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.home.adapter.CardStackAdapter
import com.starters.hsge.presentation.main.home.network.RetrofitApi
import com.yuyakaido.android.cardstackview.*
import retrofit2.Response
import retrofit2.Retrofit

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager
    private lateinit var dogCardList: List<DogCard>

    // 각각 통신해야해서 쓸모 ㄴㄴ
    //private val likeDog = mutableListOf<Int>()// '좋아요 한 강아지 id' 담을 '좋아요 강아지' 리스트
    //private val dislikeDog = mutableListOf<Int>()

    private var isLike: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofitWork()


//        dogCardList = listOf<DogCard>(
//            DogCard(
//                "뽀삐",
//                1,
//                "https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg",
//                "스탠다드 푸들",
//                " I 12살 10개월 I ",
//                "Female",
//                true,
//                Tag(listOf("고구마랄라", "간식감자", "수영좋아"), listOf("산책", "카메라"))
//            ),
//            DogCard(
//                "뽀삐",
//                2,
//                "https://i.guim.co.uk/img/media/fe1e34da640c5c56ed16f76ce6f994fa9343d09d/0_174_3408_2046/master/3408.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=0d3f33fb6aa6e0154b7713a00454c83d",
//                "스탠다드 푸들",
//                " I 12살 10개월 I ",
//                "Female",
//                true,
//                Tag(listOf("고구마", "간식", "수영"), listOf("산책", "카메라"))
//            ),
//            DogCard(
//                "뽀삐",
//                3,
//                "https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg",
//                "스탠다드 푸들",
//                " I 12살 10개월 I ",
//                "Female",
//                true,
//                Tag(listOf("고구마랄라", "간식감자", "수영좋아"), listOf("산책", "카메라"))
//            ),
//            DogCard(
//                "뽀삐",
//                4,
//                "https://i.guim.co.uk/img/media/fe1e34da640c5c56ed16f76ce6f994fa9343d09d/0_174_3408_2046/master/3408.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=0d3f33fb6aa6e0154b7713a00454c83d",
//                "스탠다드 푸들",
//                " I 12살 10개월 I ",
//                "Female",
//                true,
//                Tag(listOf("고구마", "간식", "수영"), listOf("산책", "카메라"))
//            ),
//            DogCard(
//                "뽀삐",
//                5,
//                "https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg",
//                "스탠다드 푸들",
//                " I 12살 10개월 I ",
//                "Female",
//                true,
//                Tag(listOf("고구마랄라", "간식감자", "수영좋아"), listOf("산책", "카메라"))
//            ),
//            DogCard(
//                "뽀삐",
//                6,
//                "https://i.guim.co.uk/img/media/fe1e34da640c5c56ed16f76ce6f994fa9343d09d/0_174_3408_2046/master/3408.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=0d3f33fb6aa6e0154b7713a00454c83d",
//                "스탠다드 푸들",
//                " I 12살 10개월 I ",
//                "Female",
//                true,
//                Tag(listOf("고구마", "간식", "수영"), listOf("산책", "카메라"))
//            ),
//            DogCard(
//                "뽀삐",
//                7,
//                "https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg",
//                "스탠다드 푸들",
//                " I 12살 10개월 I ",
//                "Female",
//                true,
//                Tag(listOf("고구마랄라", "간식감자", "수영좋아"), listOf("산책", "카메라"))
//            ),
//            DogCard(
//                "뽀삐",
//                8,
//                "https://i.guim.co.uk/img/media/fe1e34da640c5c56ed16f76ce6f994fa9343d09d/0_174_3408_2046/master/3408.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=0d3f33fb6aa6e0154b7713a00454c83d",
//                "스탠다드 푸들",
//                " I 12살 10개월 I ",
//                "Female",
//                true,
//                Tag(listOf("고구마", "간식", "수영"), listOf("산책", "카메라"))
//            )
//        )

        manager = CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
                when (direction) {
                    Direction.Right -> like(dogCardList)
                    Direction.Left -> dislike(dogCardList)
                    else -> {

                    }
                }
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


        // 로컬 데이터
//        cardStackAdapter = CardStackAdapter(requireContext(), dogCardList)
//        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
//        binding.cardStackView.layoutManager = manager
//        binding.cardStackView.adapter = cardStackAdapter


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

    private fun retrofitWork() {
        RetrofitApi.dogService.getDogData().enqueue(object : retrofit2.Callback<List<DogCard>> {
            override fun onResponse(
                call: retrofit2.Call<List<DogCard>>,
                response: Response<List<DogCard>>
            ) {
                if (response.isSuccessful) {
                    Log.d("TAG", response.toString())
                    Log.d("TAG", "성공")
                    val dogCardResult = response.body()
                    cardStackAdapter = CardStackAdapter(requireContext(), dogCardResult!!)
                    manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
                    binding.cardStackView.layoutManager = manager
                    binding.cardStackView.adapter = cardStackAdapter
                } else {
                    Log.d("TAG", "실패")
                    Log.d("TAG", response.code().toString())
                }

            }

            override fun onFailure(call: retrofit2.Call<List<DogCard>>, t: Throwable) {
                Log.d("TAG", "실패")
                Log.d("TAG", t.toString())
            }
        })
    }

    private fun like(dogCardList: List<DogCard>) {
        val card = dogCardList[manager.topPosition - 1] // 카트스택의 최 상위를 찾은다음에 뺴줘야함
        isLike = true
        //Toast.makeText(requireContext(), "좋아요 : ${card.dogId}", Toast.LENGTH_SHORT).show()
        // post 통신 진행

        //likeDog.add(card.dogId)
        //Log.d("좋아요강아지", "좋아요 : ${likeDog.toString()}")
    }

    private fun dislike(dogCardList: List<DogCard>) {
        val card = dogCardList[manager.topPosition - 1]
        isLike = false
        //Toast.makeText(requireContext(), "싫어요 : ${card.dogId}", Toast.LENGTH_SHORT).show()
        // post 통신 진행

        //dislikeDog.add(card.dogId)
        //Log.d("싫어요강아지", "싫어요 : ${dislikeDog.toString()}")
    }


}
