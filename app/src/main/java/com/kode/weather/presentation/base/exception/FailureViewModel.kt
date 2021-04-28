package com.kode.weather.presentation.base.exception

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.kode.weather.domain.base.exception.FailureInfo
import com.kode.weather.presentation.base.BaseViewModel

class FailureViewModel constructor(savedStateHandle: SavedStateHandle): BaseViewModel() {

    companion object {
        const val FAILURE_INFO_KEY = "failure info"
    }

    val failureInfo: LiveData<FailureInfo> =
        savedStateHandle.getLiveData(FAILURE_INFO_KEY)
}