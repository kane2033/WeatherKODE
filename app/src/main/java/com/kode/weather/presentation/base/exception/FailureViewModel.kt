package com.kode.weather.presentation.base.exception

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kode.weather.domain.base.exception.info.FullScreenFailureInfo

class FailureViewModel(failureInfo: FullScreenFailureInfo) : ViewModel() {

    val failureInfo: LiveData<FullScreenFailureInfo> = liveData { emit(failureInfo) }
}