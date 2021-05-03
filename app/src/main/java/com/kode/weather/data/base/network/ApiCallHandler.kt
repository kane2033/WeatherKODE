package com.kode.weather.data.base.network

import com.kode.weather.domain.base.exception.Failure
import retrofit2.Response

class ApiCallHandler(private val networkHandler: NetworkHandler) {

    fun <T, R> getBodySafely(response: Response<T?>, transform: (T) -> R): R {
        // Проверка на наличие доступа к сети и возвращение ошибки при отсутствии
        if (!networkHandler.isNetworkAvailable()) throw Failure.NetworkConnection

        // Возвращаем ошибку, если результат запроса [300; 500]
        if (!response.isSuccessful) throw Failure.RequestFailure(code = response.code())

        // Если тело результата запроса пусто, возвращаем ошибку
        val responseBody: T = response.body() ?: throw Failure.MissingContentFailure

        // Все проверки пройдены, возвращаем результат
        return transform(responseBody)
    }
}