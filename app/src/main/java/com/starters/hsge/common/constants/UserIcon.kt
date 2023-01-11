package com.starters.hsge.common.constants

import com.starters.hsge.R

enum class UserIcon(val resId: Int, val order: Int) {
    ICON_FIRST(R.drawable.ic_dog_profile_1, 1),
    ICON_SECOND(R.drawable.ic_dog_profile_2, 2),
    ICON_THIRD(R.drawable.ic_dog_profile_3, 3),
    ICON_FORTH(R.drawable.ic_dog_profile_4, 4),
    ICON_FIFTH(R.drawable.ic_dog_profile_5, 5),
    ICON_SIXTH(R.drawable.ic_dog_profile_6, 6),
    ICON_SEVENTH(R.drawable.ic_dog_profile_7, 7),
    ICON_EIGHTH(R.drawable.ic_dog_profile_8, 8),
    ICON_NINTH(R.drawable.ic_dog_profile_9, 9),
    ICON_TENTH(R.drawable.ic_dog_profile_10, 10),
    ICON_ELEVENTH(R.drawable.ic_dog_profile_11, 11),
    ICON_TWELFTH(R.drawable.ic_dog_profile_12, 12),
    ICON_THIRTEENTH(R.drawable.ic_dog_profile_13, 13),
    ICON_FOURTEENTH(R.drawable.ic_dog_profile_14, 14),
    ICON_FIFTEENTH(R.drawable.ic_dog_profile_15, 15);
}

fun Int.orderToIcon(): Int =
    UserIcon.values().single {
        this == it.order
    }.resId

fun Int.iconToOrder(): Int =
    UserIcon.values().single {
        this == it.resId
    }.order
