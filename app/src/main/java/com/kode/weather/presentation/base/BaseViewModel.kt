package com.kode.weather.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.base.exception.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

interface BaseViewModel {
    // Статус загрузки (используется с прогресс баром)
    val _isLoading: MutableLiveData<Boolean>
    val isLoading: LiveData<Boolean>

    // Поток запускается - отображается загрузка
    // Поток останавливается - загрузка не показывается
    fun <T> Flow<T>.loadingIndication() =
        onStart { _isLoading.value = true }
            .onCompletion { _isLoading.value = false }

    // Упаковка ошибки в event, чтобы
    // отображение ошибки возникало только один раз
    val _failure: MutableLiveData<Event<Failure>>
    val failure: LiveData<Event<Failure>>

    // Стандартный метод обработки ошибки, упаковывающий ее
    // в event
    fun handleFailure(throwable: Throwable) {
        if (throwable is Failure) {
            _failure.value = Event(throwable)
        }
    }

    // Создание LiveData из MutableLiveData,
    // чтобы скрыть MutableLiveData от view
    fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>

    fun <T> Flow<T>.catchFailure() = catch { e -> if (e is Failure) handleFailure(e) }

}