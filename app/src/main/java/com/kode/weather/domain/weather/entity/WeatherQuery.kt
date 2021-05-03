package com.kode.weather.domain.weather.entity

data class WeatherQuery(
    val cityName: String,
    val tempMeasurement: TempMeasurement = TempMeasurement.METRIC
) {
}