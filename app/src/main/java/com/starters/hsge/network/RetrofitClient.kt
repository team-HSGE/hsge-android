package com.starters.hsge.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    //Retrofit.Builder 클래스의 인스턴스를 만들고 Base URL을 구성해준다.
    val sRetrofit = initRetrofit()
    private const val AccessToken_URL = "http://localhost:8082"

    private fun initRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(AccessToken_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaTypeOrNull()!!))
            .build()
}