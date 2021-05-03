package com.kode.weather.domain.weather.usecase

import com.kode.weather.domain.base.usecase.UseCase
import com.kode.weather.domain.weather.datasource.WeatherDataSource
import com.kode.weather.domain.weather.entity.Weather
import com.kode.weather.domain.weather.entity.WeatherQuery

class FetchCityWeather(private val weatherDataSource: WeatherDataSource) :
    UseCase<Weather, WeatherQuery>() {
    override fun run(param: WeatherQuery): Weather {
        return weatherDataSource.getWeatherByCityName(param)
    }
}