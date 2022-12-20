package com.starters.hsge.data.interfaces

import com.starters.hsge.data.model.remote.request.NicknameRequest
import com.starters.hsge.data.model.remote.response.NicknameResponse

interface NicknameInterface {

    fun onPostUserNicknameSuccess(nicknameResponse: NicknameResponse, isSuccess: Boolean, code: Int, nicknameRequest: NicknameRequest)

    fun onPostUserNicknameFailure(message: String)
}