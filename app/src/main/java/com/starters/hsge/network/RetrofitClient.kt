package com.starters.hsge.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    //Retrofit.Builder 클래스의 인스턴스를 만들고 Base URL을 구성해준다.
    val sRetrofit = initRetrofit()
    private const val AccessToken_URL = "http://192.168.0.144:8081/"

    private fun initRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(AccessToken_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}