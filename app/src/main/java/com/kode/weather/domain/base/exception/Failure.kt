package com.kode.weather.domain.base.exception

/**
 * Базовый класс для обработки ошибок.
 * Каждая ошибка, связанная с api запросами, должна наследовать [RequestFailure]
 * Каждая ошибка, специфичная для для функции, должна наследовать [FeatureFailure].
 */
sealed class Failure: Throwable() {
    object NetworkConnection : Failure()
    object MissingContentFailure : Failure()

    open class FeatureFailure : Failure()

    open class RequestFailure(val code: Int, override val message: String? = null) : Failure() {
        override fun equals(other: Any?) = other is RequestFailure && other.code == this.code
        override fun hashCode() = code
    }
}