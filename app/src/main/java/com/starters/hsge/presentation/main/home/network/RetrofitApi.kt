package com.starters.hsge.presentation.main.home.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitApi {
    private const val BASE_URL = "http://192.168.0.129:8080"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(
            RequestInterceptor()
        ).build()
    }

     val retrofit: Retrofit by lazy {
        Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    class RequestInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()

            builder.addHeader(
                "Authorization",
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzMxNTkxOTEsImlhdCI6MTY3MDU2NzE5MCwiZW1haWwiOiJqdW5ueWFubmU4MDZAbmF2ZXIuY29tIn0.7xKfTuV8LPjuB664Kkr0renESuerVlyTHyw-sJ-F0b0"
            )
            return chain.proceed(builder.build())
        }
    }

    val dogService: DogService by lazy {
        retrofit.create(DogService::class.java)
    }



}