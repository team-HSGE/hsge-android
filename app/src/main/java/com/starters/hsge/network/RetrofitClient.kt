package com.starters.hsge.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import java.io.IOException
import com.starters.hsge.App.Companion.prefs
import com.starters.hsge.presentation.common.constants.AUTHORIZATION
import com.starters.hsge.presentation.common.constants.BEARER_ACCESS_TOKEN

object RetrofitClient {

    //Retrofit.Builder 클래스의 인스턴스를 만들고 Base URL을 구성해준다.
    val sRetrofit = initRetrofit()

    private const val AccessToken_URL = "https://prod.hsge.site/"
    //private const val AccessToken_URL = "https://dev2.hsge.site/" // 테스트 서버


    private fun initRetrofit() : Retrofit =
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

            val bearerJwt: String? = prefs.getString(BEARER_ACCESS_TOKEN, "")

            if (bearerJwt != null) {
                builder.addHeader(AUTHORIZATION, bearerJwt)
            }
            return chain.proceed(builder.build())
        }
    }
}