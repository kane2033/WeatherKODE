package com.kode.weather.presentation.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kode.weather.data.weather.WeatherMockDataSource
import com.kode.weather.domain.weather.entity.WeatherInfo
import com.kode.weather.domain.weather.usecase.FetchWeatherInfo
import com.kode.weather.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel() : BaseViewModel() {

    private val fetchWeatherInfo = FetchWeatherInfo(WeatherMockDataSource())

    private val _weatherInfo = MutableLiveData<WeatherInfo>()
    val weatherInfo: LiveData<WeatherInfo> = _weatherInfo

    init {
        fetchWeatherInfo()
    }

    private fun fetchWeatherInfo() {
        viewModelScope.launch {
            val result = fetchWeatherInfo(Unit).loadingIndication().single()
            result.fold(
                onSuccess = { _weatherInfo.value = it },
                onFailure = { handleFailure(it) }
            )
        }
    }
}