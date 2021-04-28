package com.kode.weather.domain.weather.datasource

import com.kode.weather.domain.weather.entity.WeatherInfo

interface WeatherDataSource {
    fun getWeatherInfo(): WeatherInfo
}