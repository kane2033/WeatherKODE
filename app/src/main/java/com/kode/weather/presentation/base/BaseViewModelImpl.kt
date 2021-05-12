package com.kode.weather.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.base.exception.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class BaseViewModelImpl : BaseViewModel {

    // Статус загрузки (используется с прогресс баром)
    override val _isLoading = MutableLiveData(false)
    override val isLoading: LiveData<Boolean> = _isLoading.asLiveData()

    override val _failure = MutableLiveData<Event<Failure>>()
    override val failure = _failure.asLiveData()

    // Поток запускается - отображается загрузка
    // Поток останавливается - загрузка не показывается
    override fun <T> Flow<T>.loadingIndication() =
        onStart { _isLoading.value = true }
            .onCompletion { _isLoading.value = false }

    // Стандартный метод обработки ошибки, упаковывающий ее
    // в event
    override fun handleFailure(throwable: Throwable) {
        if (throwable is Failure) {
            _failure.value = Event(throwable)
        }
    }

    // Создание LiveData из MutableLiveData,
    // чтобы скрыть MutableLiveData от view
    override fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>
}