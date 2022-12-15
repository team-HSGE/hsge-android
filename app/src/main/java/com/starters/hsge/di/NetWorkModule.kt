package com.starters.hsge.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.starters.hsge.App
import com.starters.hsge.data.api.*
import com.starters.hsge.presentation.common.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
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

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun providesHeaderInterceptor() = Interceptor { chain ->
        with(chain) {
            val request = request().newBuilder()
            val bearerJwt: String? = App.prefs.getString("BearerAccessToken", "")

            if (bearerJwt != null) {
                request
                    .addHeader("Authorization", bearerJwt)
            }
            proceed(request.build())
        }
    }

    @Provides
    @Singleton
    fun providesLoggerInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

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

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesConvertorFactory() =
        json.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()

    // multipart API
    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesApi(@RetrofitHSGE retrofit: Retrofit): ImageService =
        retrofit.create(ImageService::class.java)

    // DogOption API
    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesDogOptionApi(@RetrofitHSGE retrofit: Retrofit): DogOptionApi =
        retrofit.create(DogOptionApi::class.java)

    // User Api
    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesUserApi(@RetrofitHSGE retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    // MyDog Api
    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesMyDogApi(@RetrofitHSGE retrofit: Retrofit): MyDogApi =
        retrofit.create(MyDogApi::class.java)

    // EditDog Api
    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesEditDogApi(@RetrofitHSGE retrofit: Retrofit): EditDogApi =
        retrofit.create(EditDogApi::class.java)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitHSGE
