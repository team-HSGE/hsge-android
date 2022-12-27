package com.starters.hsge.presentation.main.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.starters.hsge.R
import com.starters.hsge.data.model.remote.response.DogCard
import com.starters.hsge.data.model.remote.request.IsLikeRequest
import com.starters.hsge.databinding.FragmentHomeBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.home.adapter.CardStackAdapter
import com.starters.hsge.data.interfaces.HomeDogInterface
import com.starters.hsge.data.interfaces.IsLikeInterface
import com.starters.hsge.data.service.HomeDogService
import com.starters.hsge.data.service.IsLikeService
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), HomeDogInterface,
    IsLikeInterface {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager

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

        CoroutineScope(Dispatchers.IO).launch {
            HomeDogService(this@HomeFragment).tryGetHomeDog()
            Log.d("순서", "tryGetHomeDog")
        }

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

    private fun swipeIsLike(dogCardList: List<DogCard>, isLike: Boolean) {
        val card = dogCardList[manager.topPosition - 1] // 카트스택의 최 상위를 찾은다음에 뺴줘야함
        Log.d("isLike?", isLike.toString())
        Log.d("isLike?", card.petId.toString())
        IsLikeService(this).tryPostIsLike(card.petId, IsLikeRequest(isLike))
    }

    // HomeDog 통신
    override fun onGetHomeDogSuccess(
        DogCardResponse: List<DogCard>,
        isSuccess: Boolean,
        code: Int
    ) {
        if (isSuccess) {

            val dogCardResult = DogCardResponse
            cardStackAdapter = CardStackAdapter(requireContext(), dogCardResult)

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

            Log.d("TAG", DogCardResponse.toString())
            Log.d("TAG", "성공")

        } else {
            Log.d("HomeDog 오류", "Error code : ${code}")

        }
    }

    override fun onGetHomeDogFailure(message: String) {
        Log.d("HomeDog 오류", "오류: $message")
    }

    // isLike 통신
    override fun onPostIsLikeSuccess(isSuccess: Boolean, code: Int) {
        if (isSuccess) {
            Log.d("IsLike", "성공")
        } else {
            Log.d("IsLike 오류", "Error code : ${code}")
        }
    }

    override fun onPostIsLikeFailure(message: String) {
        Log.d("IsLike 오류", "오류: $message")
    }
}