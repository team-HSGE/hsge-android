package com.starters.hsge.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.starters.hsge.App
import com.starters.hsge.data.api.*
import com.starters.hsge.presentation.common.constants.AUTHORIZATION
import com.starters.hsge.presentation.common.constants.BASE_URL
import com.starters.hsge.presentation.common.constants.BEARER_ACCESS_TOKEN
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
            val bearerJwt: String? = App.prefs.getString(BEARER_ACCESS_TOKEN, "")

            if (bearerJwt != null) {
                request
                    .addHeader(AUTHORIZATION, bearerJwt)
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

    // DogOption API
    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesDogOptionApi(@RetrofitHSGE retrofit: Retrofit): DogOptionApi =
        retrofit.create(DogOptionApi::class.java)

    // SignUp Api
    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesSignUpApi(@RetrofitHSGE retrofit: Retrofit): SignUpApi =
        retrofit.create(SignUpApi::class.java)

    // UserApi
    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesUserApi(@RetrofitHSGE retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    // UserDog Api
    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesMyDogApi(@RetrofitHSGE retrofit: Retrofit): UserDogApi =
        retrofit.create(UserDogApi::class.java)


    // Chat Api
    @Provides
    @Singleton
    @RetrofitHSGE
    fun provideChatRecordApi(@RetrofitHSGE retrofit: Retrofit): ChatApi =
        retrofit.create(ChatApi::class.java)

    // Partner Api
    @Provides
    @Singleton
    @RetrofitHSGE
    fun providesPartnerApi(@RetrofitHSGE retrofit: Retrofit): PartnerApi =
        retrofit.create(PartnerApi::class.java)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitHSGE
