package com.starters.hsge.data.service

import android.util.Log
import android.view.View
import com.starters.hsge.data.api.HomeDogApi
import com.starters.hsge.data.interfaces.HomeDogInterface
import com.starters.hsge.data.model.remote.response.CheckTokenResponse
import com.starters.hsge.data.model.remote.response.DogCard
import com.starters.hsge.network.RetrofitClient
import com.starters.hsge.presentation.main.home.adapter.CardStackAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.SwipeableMethod
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeDogService(val homeDogInterface: HomeDogInterface) {

    fun tryGetHomeDog() {
        val dogCardRetrofit = RetrofitClient.sRetrofit.create(HomeDogApi::class.java)
        dogCardRetrofit.getDogData().enqueue(object : Callback<List<DogCard>> {
            override fun onResponse(
                call: Call<List<DogCard>>,
                response: Response<List<DogCard>>
            ) {
                homeDogInterface.onGetHomeDogSuccess(response.body() , response.isSuccessful, response.code())
            }

            override fun onFailure(call: retrofit2.Call<List<DogCard>>, t: Throwable) {
                homeDogInterface.onGetHomeDogFailure(t.message ?: "통신 오류")
            }
        })
    }
}