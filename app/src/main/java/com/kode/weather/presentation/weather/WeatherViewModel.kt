package com.kode.weather.presentation.weather

import androidx.lifecycle.liveData
import com.kode.weather.presentation.base.BaseViewModel

class WeatherViewModel(cityName: String) : BaseViewModel() {

    val city = liveData<String> { emit(cityName) }
}