package com.kode.weather.data.base.network.extention

import com.kode.weather.domain.base.exception.Failure
import okio.IOException
import retrofit2.Call
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Безопасный вызов [Call.execute], обрабатывающий возможные ошибки.
 * @param [transformBody] - если запрос успешен, перевод тела из [T] в [R].
 * @param [transformRequestFailure] - трансформация HTTP ошибок ([300; 500]) в доменные [Failure].
 * @return Тело запроса [Response.body] при успехе
 * @throws [Failure.NetworkFailure] при ошибке на сервере
 * */
fun <T, R> Call<T?>.executeSafely(
    transformBody: (T) -> R,
    transformRequestFailure: (requestFailure: Failure.RequestFailure) -> Failure
): R {
    // Получение результата с проверкой серверных ошибок
    val response = try {
        execute()
    } catch (e: IOException) {
        throw when (e) {
            is SocketTimeoutException -> Failure.Timeout
            else -> Failure.ServerFailure
        }
    }
    // Возвращаем ошибку, если результат запроса [300; 500]
    if (!response.isSuccessful) throw transformRequestFailure(Failure.RequestFailure(response.code()))

    // Если тело результата запроса пусто, возвращаем ошибку
    val responseBody: T = response.body() ?: throw Failure.MissingContentFailure

    // Все проверки пройдены, возвращаем результат
    return transformBody(responseBody)
}