package com.starters.hsge.data.interfaces

interface IsLikeInterface {

    fun onPostIsLikeSuccess(isSuccess: Boolean, code: Int)

    fun onPostIsLikeFailure(message: String)

}