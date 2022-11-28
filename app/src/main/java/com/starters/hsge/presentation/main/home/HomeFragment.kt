package com.starters.hsge.presentation.main.home

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.starters.hsge.R
import com.starters.hsge.data.model.DogCard
import com.starters.hsge.data.model.Tag
import com.starters.hsge.databinding.FragmentHomeBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.main.home.adapter.CardStackAdapter
import com.yuyakaido.android.cardstackview.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardStackView = view.findViewById<CardStackView>(R.id.card_stack_view)

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

        val dogCardList = listOf<DogCard>(
            DogCard(
                "뽀삐",
                "https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg",
                "스탠다드 푸들",
                " I 12살 10개월 I ",
                true,
                Tag(listOf("고구마", "간식"), listOf("산책"))
            ),
            DogCard(
                "뚜뚜",
                "https://user-images.githubusercontent.com/106398273/204195922-421c77ad-111c-4981-9dfa-a65d532ad132.jpeg",
                "스탠다드 푸들",
                " I 12살 10개월 I ",
                true,
                Tag(listOf("고구마", "간식"), listOf("산책"))
            ),
            DogCard(
                "뽀삐2",
                "https://user-images.githubusercontent.com/106398273/204196158-10c88c38-8e6e-4412-afd0-03bb376d6377.jpeg",
                "스탠다드 푸들",
                " I 12살 10개월 I ",
                true,
                Tag(listOf("고구마", "간식"), listOf("산책"))
            ),
            DogCard(
                "뚜뚜2",
                "https://user-images.githubusercontent.com/106398273/204196865-d27a4994-fc3f-4380-a569-be4f58e7c354.jpeg",
                "스탠다드 푸들",
                " I 12살 10개월 I ",
                true,
                Tag(listOf("고구마", "간식"), listOf("산책"))
            ),
            DogCard(
                "뽀삐2",
                "https://user-images.githubusercontent.com/106398273/204196158-10c88c38-8e6e-4412-afd0-03bb376d6377.jpeg",
                "스탠다드 푸들",
                " I 12살 10개월 I ",
                true,
                Tag(listOf("고구마", "간식"), listOf("산책"))
            ),
            DogCard(
                "뚜뚜2",
                "https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg",
                "스탠다드 푸들",
                " I 12살 10개월 I ",
                true,
                Tag(listOf("고구마", "간식"), listOf("산책"))
            )
        )

        cardStackAdapter = CardStackAdapter(requireContext(), dogCardList)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter

        fabClick(cardStackView)
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
}