package com.kode.weather.data.weather.datasource.network.converter

import com.kode.weather.data.weather.datasource.network.model.WeatherByCityDto
import com.kode.weather.domain.weather.entity.Weather
import com.kode.weather.domain.weather.entity.WindDirection
import java.util.*
import kotlin.math.roundToInt

fun WeatherByCityDto.toWeather() =
    Weather(
        main?.temp?.roundToInt() ?: 0,
        weather?.get(0)?.icon ?: "",
        weather?.get(0)?.description?.capitalizeWords() ?: "",
        main?.humidity?.toInt() ?: 0,
        wind?.speed ?: 0.0,
        wind?.deg?.toInt()?.let { WindDirection.degreeToDirection(it) } ?: WindDirection.NORTH,
        main?.pressure?.toInt() ?: 0
    )

// Превращает все буквы в слове в заглавные
private fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.capitalize(Locale.getDefault()) }