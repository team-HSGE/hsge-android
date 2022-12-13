package com.starters.hsge.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import java.io.IOException
import com.starters.hsge.App.Companion.prefs

object RetrofitClient {

    //Retrofit.Builder 클래스의 인스턴스를 만들고 Base URL을 구성해준다.
    val sRetrofit = initRetrofit()
    private const val AccessToken_URL = "http://ec2-3-38-191-250.ap-northeast-2.compute.amazonaws.com/"

    private fun initRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(AccessToken_URL)
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaTypeOrNull()!!))
            .build()

    private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient =
        OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder: Request.Builder = chain.request().newBuilder()

            val bearerJwt: String? = prefs.getString("BearerAccessToken", "")

            if (bearerJwt != null) {
                builder.addHeader("Authorization", bearerJwt)
            }
            return chain.proceed(builder.build())
        }
    }
}
