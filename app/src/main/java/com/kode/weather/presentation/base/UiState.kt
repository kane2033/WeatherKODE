package com.kode.weather.presentation.base

import com.kode.weather.domain.base.exception.Failure as ThrowableFailure

sealed class UiState {
    object Success : UiState()
    data class Failure(val failure: ThrowableFailure) : UiState()
    object Loading : UiState()
}