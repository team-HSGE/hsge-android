package com.starters.hsge.data.service

import com.starters.hsge.data.api.IsLikeApi
import com.starters.hsge.data.interfaces.IsLikeInterface
import com.starters.hsge.data.model.remote.request.IsLikeRequest
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IsLikeService(val isLikeInterface: IsLikeInterface) {

    fun tryPostIsLike(petId: Int, isLikeRequest: IsLikeRequest){
        val isLikeRetrofit = RetrofitClient.sRetrofit.create(IsLikeApi::class.java)

        isLikeRetrofit.postIsLikeData(petId, isLikeRequest).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                isLikeInterface.onPostIsLikeSuccess(response.isSuccessful, response.code())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                isLikeInterface.onPostIsLikeFailure(t.message ?: "통신 오류")
            }
        })
    }
}