package com.kode.weather.domain.base

/**
 * Класс используется как обертка для данных,
 * которые используются типом LiveData
 * и представляют собой событие (Event).
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Запрет изменения свойства извне

    /**
     * Возвращает данные и предотвращает повторное использование.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Возвращает данные, даже если они уже взяты.
     */
    fun peekContent(): T = content
}