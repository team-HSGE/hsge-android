package com.starters.hsge.data.interfaces

interface ChatExitInterface {

    fun onPostChatExitSuccess(isSuccess: Boolean, code: Int)

    fun onPostChatExitFailure(message: String)
}