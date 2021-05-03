package com.kode.weather.data.weather.datasource.network.model

import com.squareup.moshi.Json

data class TemperatureDto(
    val temp: Double?,

    @Json(name = "feels_like")
    val feelsLike: Double?,

    @Json(name = "temp_min")
    val tempMin: Double?,

    @Json(name = "temp_max")
    val tempMax: Double?,

    val pressure: Long?,
    val humidity: Long?
)