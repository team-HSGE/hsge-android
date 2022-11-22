package com.starters.hsge.di

import com.starters.hsge.presentation.common.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addNetworkInterceptor(httpLoggingInterceptor)
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()


    /**
     * Retrofit, api 예제
     */
    @Provides
    @Singleton
    @RetrofitBase
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()


    // API
//    @Provides
//    @Singleton
//    @RetrofitBase
//    fun providesApi(@RetrofitBase retrofit: Retrofit): Api =
//        retrofit.create(Api::class.java)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitBase
