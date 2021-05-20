package com.kode.weather.di.base

import com.kode.weather.BuildConfig
import com.kode.weather.data.base.network.NetworkAvailabilityInterceptor
import com.kode.weather.data.base.network.NetworkHandler
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {
    val module = module {
        single { NetworkHandler(androidContext()) }
        single { NetworkAvailabilityInterceptor(get()) }

        single { provideOkHttpClient(get()) }
        single { provideRetrofit(get()) }
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(MoshiConverterFactory.create(provideMoshi())) // Парсинг JSON
        .build()

    fun provideOkHttpClient(
        networkAvailabilityInterceptor: NetworkAvailabilityInterceptor
    ): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        // Логирование запросов в консоль
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }

        // Проверка сети при каждом запросе
        okHttpClientBuilder.addInterceptor(networkAvailabilityInterceptor)

        return okHttpClientBuilder.build()
    }

    // JSON парсер с доп. адаптером для kotlin
    private fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

}