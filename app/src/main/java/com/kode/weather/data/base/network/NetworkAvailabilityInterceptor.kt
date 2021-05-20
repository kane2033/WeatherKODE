package com.kode.weather.data.base.network

import com.kode.weather.domain.base.exception.Failure
import okhttp3.Interceptor
import okhttp3.Response

/**
 * [Interceptor], проверяющий наличие сети и выбрасывающий [Failure.NoInternet]
 * при отсутствии сети.
 * */
class NetworkAvailabilityInterceptor(private val networkHandler: NetworkHandler) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkHandler.isNetworkAvailable()) throw Failure.NoInternet

        return chain.proceed(chain.request())
    }
}