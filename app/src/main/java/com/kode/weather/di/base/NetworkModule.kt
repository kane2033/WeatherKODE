package com.kode.weather.di.base

import com.kode.weather.BuildConfig
import com.kode.weather.data.base.network.NetworkAvailabilityInterceptor
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

        single { NetworkAvailabilityInterceptor(get()) }
        single { OpenWeatherAuthInterceptor() }
        single { provideOkHttpClient(get(), get()) }
        single { provideRetrofit(get(), provideMoshi()) }
    }

    private fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // Парсинг JSON
        .build()

    private fun provideOkHttpClient(
        networkAvailabilityInterceptor: NetworkAvailabilityInterceptor,
        openWeatherAuthInterceptor: OpenWeatherAuthInterceptor
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
        // Добавление токена в каждый запрос
        okHttpClientBuilder.addInterceptor(openWeatherAuthInterceptor)

        return okHttpClientBuilder.build()
    }

    // JSON парсер с доп. адаптером для kotlin
    private fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

}