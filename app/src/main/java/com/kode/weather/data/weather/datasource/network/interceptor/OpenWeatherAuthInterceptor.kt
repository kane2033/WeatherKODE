package com.kode.weather.data.weather.datasource.network.interceptor

import com.kode.weather.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * [Interceptor], добавляющий API токен сервиса OpenWeatherMapAPI
 * в каждый запрос
 * */
class OpenWeatherAuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter("appid", BuildConfig.OPEN_WEATHER_API_KEY).build()
        val newRequest = request.newBuilder().url(url).build()
        return chain.proceed(newRequest)
    }
}