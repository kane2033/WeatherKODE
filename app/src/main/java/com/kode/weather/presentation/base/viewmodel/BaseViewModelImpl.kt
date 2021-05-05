package com.kode.weather.presentation.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.base.exception.Failure

class BaseViewModelImpl : BaseViewModel {

    // Статус загрузки (используется с прогресс баром)
    override val _isLoading = MutableLiveData(false)
    override val isLoading: LiveData<Boolean> = _isLoading.asLiveData()

    // Поток запускается - отображается загрузка
    // Поток останавливается - загрузка не показывается
    override val _failure = MutableLiveData<Event<Failure>>()
    override val failure = _failure.asLiveData()
}