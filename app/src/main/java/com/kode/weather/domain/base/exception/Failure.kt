package com.kode.weather.domain.base.exception

/**
 * Базовый класс для обработки ошибок.
 * Каждая ошибка, связанная с api запросами, должна наследовать [RequestFailure]
 * Каждая ошибка, специфичная для для функции, должна наследовать [FeatureFailure].
 */
sealed class Failure: Throwable() {
    open class FeatureFailure : Failure()

    object MissingContentFailure : Failure()

    // Общие сетевые ошибки
    open class NetworkFailure : Failure()
    object NoInternet : NetworkFailure()
    object ServerFailure : NetworkFailure()
    object Timeout : NetworkFailure()

    open class RequestFailure(val code: Int, override val message: String? = null) :
        NetworkFailure() {
        override fun equals(other: Any?) = other is RequestFailure && other.code == this.code
        override fun hashCode() = code
    }
}