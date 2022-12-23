package com.starters.hsge.data.interfaces

interface distanceInterface {

    fun onPostDistanceSuccess(isSuccess: Boolean, code: Int)

    fun onPostIsLikeFailure(message: String)
}