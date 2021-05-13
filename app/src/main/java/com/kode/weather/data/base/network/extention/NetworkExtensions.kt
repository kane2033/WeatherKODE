package com.kode.weather.data.base.network.extention

import com.kode.weather.domain.base.exception.Failure
import retrofit2.Response

fun <T, R> Response<T?>.getBodySafely(
    transform: (T) -> R,
    requestFailure: (requestFailure: Failure.RequestFailure) -> Failure
): R {
    // Возвращаем ошибку, если результат запроса [300; 500]
    if (!isSuccessful) throw requestFailure(Failure.RequestFailure(code = code()))

    // Если тело результата запроса пусто, возвращаем ошибку
    val responseBody: T = body() ?: throw Failure.MissingContentFailure

    // Все проверки пройдены, возвращаем результат
    return transform(responseBody)
}