package com.starters.hsge.presentation.main.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.starters.hsge.R
import com.starters.hsge.data.api.HomeDogApi
import com.starters.hsge.data.model.remote.response.DogCard
import com.starters.hsge.data.model.remote.request.IsLikeRequest
import com.starters.hsge.databinding.FragmentHomeBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.home.adapter.CardStackAdapter
import com.starters.hsge.data.api.IsLikeApi
import com.starters.hsge.network.RetrofitClient
import com.yuyakaido.android.cardstackview.*
import retrofit2.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager
    private lateinit var dogCardList: List<DogCard>

    private var isLike: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("firebaseToken", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("firebase", token.toString())

        })

        retrofitWork()
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

    // 수정 필요
    private fun retrofitWork() {
        val dogCardRetrofit = RetrofitClient.sRetrofit.create(HomeDogApi::class.java)
        dogCardRetrofit.getDogData().enqueue(object : Callback<List<DogCard>> {
            override fun onResponse(
                call: Call<List<DogCard>>,
                response: Response<List<DogCard>>
            ) {
                if (response.isSuccessful) {
                    Log.d("TAG", response.toString())
                    Log.d("TAG", "성공")
                    val dogCardResult = response.body()
                    cardStackAdapter = CardStackAdapter(requireContext(), dogCardResult!!)

                    manager = CardStackLayoutManager(context, object : CardStackListener {
                        override fun onCardDragging(direction: Direction?, ratio: Float) {
                        }

                        override fun onCardSwiped(direction: Direction?) {
                            when (direction) {
                                Direction.Right -> swipeIsLike(dogCardResult, isLike = true)
                                Direction.Left -> swipeIsLike(dogCardResult, isLike = false)
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

    private fun swipeIsLike(dogCardList: List<DogCard>, isLike: Boolean) {
        val card = dogCardList[manager.topPosition - 1] // 카트스택의 최 상위를 찾은다음에 뺴줘야함
        Log.d("isLike?", isLike.toString())
        Log.d("isLike?", card.petId.toString())
        postIsLikeRetrofitWork(card.petId, isLike)
    }

    private fun postIsLikeRetrofitWork(petId: Int, isLike: Boolean){
        val isLikeRetrofit = RetrofitClient.sRetrofit.create(IsLikeApi::class.java)

        isLikeRetrofit.postIsLikeData(petId = petId, request = IsLikeRequest(isLike)).enqueue(object: Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("isLike", response.toString())
                    Log.d("isLike", "성공")
                }else{
                    Log.d("isLike", response.code().toString())
                    Log.d("isLike", "실패")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("isLike", t.toString())
                Log.d("isLike", "실패")
            }
        })
    }
}