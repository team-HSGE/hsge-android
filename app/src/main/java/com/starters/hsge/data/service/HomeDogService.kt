package com.starters.hsge.data.service

import com.starters.hsge.data.api.HomeDogApi
import com.starters.hsge.data.interfaces.HomeDogInterface
import com.starters.hsge.data.model.remote.response.DogCard
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeDogService(val homeDogInterface: HomeDogInterface) {

     fun tryGetHomeDog() {
        val dogCardApi = RetrofitClient.sRetrofit.create(HomeDogApi::class.java)
        dogCardApi.getDogData().enqueue(object : Callback<List<DogCard>> {
            override fun onResponse(
                call: Call<List<DogCard>>,
                response: Response<List<DogCard>>
            ) {
                    response.body()
                        ?.let { homeDogInterface.onGetHomeDogSuccess(it, response.isSuccessful, response.code()) }
            }

            override fun onFailure(call: Call<List<DogCard>>, t: Throwable) {
                homeDogInterface.onGetHomeDogFailure(t.message ?: "통신 오류")
            }
        })
    }
}