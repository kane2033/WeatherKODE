package com.kode.weather.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.base.exception.Failure
import kotlinx.coroutines.flow.Flow

interface BaseViewModel {

    // Статус загрузки (используется с прогресс баром)
    val _isLoading: MutableLiveData<Boolean>
    val isLoading: LiveData<Boolean>

    // Упаковка ошибки в event, чтобы
    // отображение ошибки возникало только один раз
    val _failure: MutableLiveData<Event<Failure>>
    val failure: LiveData<Event<Failure>>

    // Поток запускается - отображается загрузка
    // Поток останавливается - загрузка не показывается
    fun <T> Flow<T>.loadingIndication(): Flow<T>

    // Стандартный метод обработки ошибки, упаковывающий ее
    // в event
    fun handleFailure(throwable: Throwable)

    // Создание LiveData из MutableLiveData,
    // чтобы скрыть MutableLiveData от view
    fun <T> MutableLiveData<T>.asLiveData(): LiveData<T>
}