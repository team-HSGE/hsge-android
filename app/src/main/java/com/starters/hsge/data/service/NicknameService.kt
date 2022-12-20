package com.starters.hsge.data.service

import com.starters.hsge.data.api.NicknameApi
import com.starters.hsge.data.interfaces.NicknameInterface
import com.starters.hsge.data.model.remote.request.NicknameRequest
import com.starters.hsge.data.model.remote.response.NicknameResponse
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NicknameService(val nicknameInterface: NicknameInterface) {

    fun tryPostNickname(nickname: NicknameRequest) {
        val nicknameApi = RetrofitClient.sRetrofit.create(NicknameApi::class.java)
        nicknameApi.postNickname(nickname).enqueue(object :
            Callback<NicknameResponse> {
            override fun onResponse(
                call: Call<NicknameResponse>,
                response: Response<NicknameResponse>
            ) {
                nicknameInterface.onPostUserNicknameSuccess(
                    response.body() as NicknameResponse,
                    response.isSuccessful,
                    response.code(),
                    nickname
                )
            }

            override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                nicknameInterface.onPostUserNicknameFailure(t.message ?: "통신 오류")
            }

        })
    }
}