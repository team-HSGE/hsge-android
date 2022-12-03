package com.starters.hsge.presentation.main.home.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitApi {
    private const val BASE_URL = "http://192.168.0.57:8080"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    val dogService: DogService by lazy {
        retrofit.create(DogService::class.java)
    }

}