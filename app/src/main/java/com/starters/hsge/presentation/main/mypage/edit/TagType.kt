package com.starters.hsge.presentation.main.mypage.edit

import com.starters.hsge.common.constants.DislikeTag
import com.starters.hsge.common.constants.LikeTag

enum class TagType(val clazz: Class<*>) {
    LIKE_TAG(LikeTag::class.java),
    DISLIKE_TAG(DislikeTag::class.java)
}
