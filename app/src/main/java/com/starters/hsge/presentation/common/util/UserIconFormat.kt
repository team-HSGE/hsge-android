package com.starters.hsge.presentation.common.util

import com.starters.hsge.common.constants.UserIcon

object UserIconFormat {
    fun applyUserIconToInt(icon: UserIcon): Int {
        return when(icon) {
            UserIcon.ICON_FIRST -> 1
            UserIcon.ICON_SECOND -> 2
            UserIcon.ICON_THIRD -> 3
            UserIcon.ICON_FORTH -> 4
            UserIcon.ICON_FIFTH -> 5
            UserIcon.ICON_SIXTH -> 6
            UserIcon.ICON_SEVENTH -> 7
            UserIcon.ICON_EIGHTH -> 8
            UserIcon.ICON_NINTH -> 9
            UserIcon.ICON_TENTH -> 10
            UserIcon.ICON_ELEVENTH -> 11
            UserIcon.ICON_TWELFTH -> 12
            UserIcon.ICON_THIRTEENTH -> 13
            UserIcon.ICON_FOURTEENTH -> 14
            UserIcon.ICON_FIFTEENTH -> 15
        }
    }

    fun applyIntToUserIcon(number: Int): UserIcon {
        return when (number) {
            1 -> UserIcon.ICON_FIRST
            2 -> UserIcon.ICON_SECOND
            3 -> UserIcon.ICON_THIRD
            4 -> UserIcon.ICON_FORTH
            5 -> UserIcon.ICON_FIFTH
            6 -> UserIcon.ICON_SIXTH
            7 -> UserIcon.ICON_SEVENTH
            8 -> UserIcon.ICON_EIGHTH
            9 -> UserIcon.ICON_NINTH
            10 -> UserIcon.ICON_TENTH
            11 -> UserIcon.ICON_ELEVENTH
            12 -> UserIcon.ICON_TWELFTH
            13 -> UserIcon.ICON_THIRTEENTH
            14 -> UserIcon.ICON_FOURTEENTH
            15 -> UserIcon.ICON_FIFTEENTH
            else -> UserIcon.ICON_FIRST
        }
    }
}