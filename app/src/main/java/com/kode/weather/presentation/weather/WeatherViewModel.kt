package com.kode.weather.presentation.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.weather.entity.Weather
import com.kode.weather.domain.weather.entity.WeatherQuery
import com.kode.weather.domain.weather.usecase.FetchCityWeather
import com.kode.weather.presentation.base.UiState
import com.kode.weather.presentation.base.asLiveData
import com.kode.weather.presentation.base.handleFailure
import com.kode.weather.presentation.base.loadingIndication
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val cityName: String,
    private val fetchCityWeather: FetchCityWeather,
) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState = _uiState.asLiveData()

    private val _weather = MutableLiveData<Weather>()
    val weather = _weather.asLiveData()

    init {
        fetchCityWeather()
    }

    fun fetchCityWeather() {
        val query = WeatherQuery(cityName)
        viewModelScope.launch {
            val result = fetchCityWeather(query).loadingIndication(_uiState).single()
            result.fold(
                onSuccess = { _weather.value = it },
                onFailure = { it.handleFailure(_uiState) }
            )
        }
    }
}