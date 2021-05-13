package com.kode.weather.domain.weather.exception

import com.kode.weather.domain.base.exception.Failure

sealed class FetchWeatherFailure : Failure.FeatureFailure() {
    object NotFound : FetchWeatherFailure()
}