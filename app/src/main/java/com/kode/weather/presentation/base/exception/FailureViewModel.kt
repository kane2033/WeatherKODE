package com.kode.weather.presentation.base.exception

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kode.weather.domain.base.exception.info.FullScreenFailureInfo
import com.kode.weather.presentation.base.BaseViewModel

class FailureViewModel(
    failureInfo: FullScreenFailureInfo,
    baseViewModel: BaseViewModel
) : BaseViewModel by baseViewModel, ViewModel() {

    val failureInfo: LiveData<FullScreenFailureInfo> = liveData { emit(failureInfo) }
}