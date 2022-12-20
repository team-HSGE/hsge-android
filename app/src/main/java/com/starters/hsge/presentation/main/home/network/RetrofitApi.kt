package com.starters.hsge.presentation.main.home.network

import com.starters.hsge.App
import com.starters.hsge.data.api.HomeDogApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitApi {
    private const val BASE_URL = "http://ec2-3-38-191-250.ap-northeast-2.compute.amazonaws.com/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(
            RequestInterceptor()
        ).build()
    }

    class RequestInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()

            val bearerJwt: String? = App.prefs.getString("BearerAccessToken", "")

            if (bearerJwt != null) {
                builder.addHeader("Authorization", bearerJwt)
            }

            return chain.proceed(builder.build())
        }
    }

    val homeDogApi: HomeDogApi by lazy {
        retrofit.create(HomeDogApi::class.java)
    }

}