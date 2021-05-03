package com.kode.weather.domain.weather.datasource

import com.kode.weather.domain.weather.entity.Weather
import com.kode.weather.domain.weather.entity.WeatherQuery

interface WeatherDataSource {
    fun getWeatherByCityName(query: WeatherQuery): Weather
}