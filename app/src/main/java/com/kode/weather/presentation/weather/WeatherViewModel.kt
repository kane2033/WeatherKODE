package com.kode.weather.presentation.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kode.weather.domain.weather.entity.Weather
import com.kode.weather.domain.weather.entity.WeatherQuery
import com.kode.weather.domain.weather.usecase.FetchCityWeather
import com.kode.weather.presentation.base.BaseViewModel
import com.kode.weather.presentation.base.BaseViewModelImpl
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val cityName: String,
    private val fetchCityWeather: FetchCityWeather,
) : BaseViewModel by BaseViewModelImpl(), ViewModel() {

    private val _weather = MutableLiveData<Weather>()
    val weather = _weather.asLiveData()

    init {
        fetchCityWeather()
    }

    fun fetchCityWeather() {
        val query = WeatherQuery(cityName)
        viewModelScope.launch {
            val result = fetchCityWeather(query).loadingIndication().single()
            result.fold(
                onSuccess = { _weather.value = it },
                onFailure = { handleFailure(it) }
            )
        }
    }
}