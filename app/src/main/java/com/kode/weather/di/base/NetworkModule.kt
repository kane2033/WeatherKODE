package com.kode.weather.di.base

import com.kode.weather.BuildConfig
import com.kode.weather.data.base.network.ApiCallHandler
import com.kode.weather.data.base.network.NetworkHandler
import com.kode.weather.data.weather.datasource.network.interceptor.OpenWeatherAuthInterceptor
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
        single { ApiCallHandler(get()) }
        single { retrofit }
    }

    // Клиент okhttp
    private val client: OkHttpClient
        get() {
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

            // Логирование запросов в консоль
            if (BuildConfig.DEBUG) {
                val loggingInterceptor =
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClientBuilder.addInterceptor(loggingInterceptor)
            }

            // Добавление токена в каждый запрос
            okHttpClientBuilder.addInterceptor(OpenWeatherAuthInterceptor())

            return okHttpClientBuilder.build()
        }

    // JSON парсер с доп. адаптером для kotlin
    private val moshi: Moshi =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // Парсинг JSON
        .build()

}