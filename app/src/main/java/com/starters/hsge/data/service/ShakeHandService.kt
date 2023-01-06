package com.starters.hsge.data.service

import com.starters.hsge.data.api.ShakeHandApi
import com.starters.hsge.data.interfaces.ShakeHandInterface
import com.starters.hsge.data.model.remote.request.CurrentLocationPostRequest
import com.starters.hsge.data.model.remote.request.UsersLocationDeleteRequest
import com.starters.hsge.data.model.remote.response.CurrentLocationPostResponse
import com.starters.hsge.data.model.remote.response.UsersLocationNearbyGetResponse
import com.starters.hsge.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShakeHandService(val shakeHandInterface: ShakeHandInterface) {

    fun tryPostCurrentLocation(currentLocation: CurrentLocationPostRequest) {
        val shakeHandApi = RetrofitClient.sRetrofit.create(ShakeHandApi::class.java)
        shakeHandApi.postUsersLocation(currentLocation).enqueue(object :
            Callback<CurrentLocationPostResponse> {
            override fun onResponse(
                call: Call<CurrentLocationPostResponse>,
                response: Response<CurrentLocationPostResponse>
            ) {
                shakeHandInterface.onPostCurrentLocationSuccess(response.body() as CurrentLocationPostResponse, response.isSuccessful)
            }

            override fun onFailure(call: Call<CurrentLocationPostResponse>, t: Throwable) {
                shakeHandInterface.onPostCurrentLocationFailure(t.message ?: "통신 오류")
            }
        })
    }

    fun tryGetHandShake() {
        val shakeHandApi = RetrofitClient.sRetrofit.create(ShakeHandApi::class.java)
        shakeHandApi.getUsersLocationNearby().enqueue(object :
            Callback<List<UsersLocationNearbyGetResponse>> {
            override fun onResponse(
                call: Call<List<UsersLocationNearbyGetResponse>>,
                response: Response<List<UsersLocationNearbyGetResponse>>
            ) {
                shakeHandInterface.onGetShakeHandSuccess(response.body() as List<UsersLocationNearbyGetResponse>, response.isSuccessful)
            }

            override fun onFailure(call: Call<List<UsersLocationNearbyGetResponse>>, t: Throwable) {
                shakeHandInterface.onGetShakeHandFailure(t.message ?: "통신 오류")
            }
        })
    }

    fun tryDeleteUsersLocation(name: UsersLocationDeleteRequest) {
        val shakeHandApi = RetrofitClient.sRetrofit.create(ShakeHandApi::class.java)
        shakeHandApi.deleteUsersLocation(name).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                shakeHandInterface.onDeleteUsersLocationSuccess(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                shakeHandInterface.onDeleteUsersLocationFailure(t.message ?: "통신 오류")
            }
        })
    }

    fun tryPostHandShake(userId: Long?) {
        val shakeHandApi = RetrofitClient.sRetrofit.create(ShakeHandApi::class.java)
        shakeHandApi.postShakeHand(userId).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                shakeHandInterface.onPostShakeHandSuccess(response.isSuccessful, response.code())
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                shakeHandInterface.onPostShakeHandFailure(t.message ?: "통신 오류")
            }
        })
    }
}