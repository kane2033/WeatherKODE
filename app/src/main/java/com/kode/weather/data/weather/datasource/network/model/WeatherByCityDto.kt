package com.kode.weather.data.weather.datasource.network.model

data class WeatherByCityDto(
    val weather: List<WeatherDto>?,
    val main: TemperatureDto?,
    val wind: WindDto?
)