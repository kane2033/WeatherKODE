package com.kode.weather.domain.weather.entity

data class Weather(
    val temperature: Int,
    val weatherCondition: WeatherCondition,
    val iconUrl: String,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val windDirection: WindDirection,
    val pressure: Int
)