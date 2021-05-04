package com.kode.weather.data.weather.datasource.network.converter

import com.kode.weather.BuildConfig
import com.kode.weather.data.weather.datasource.network.model.WeatherByCityDto
import com.kode.weather.domain.weather.entity.Weather
import com.kode.weather.domain.weather.entity.WeatherCondition
import com.kode.weather.domain.weather.entity.WindDirection
import java.util.*
import kotlin.math.roundToInt

fun WeatherByCityDto.toWeather() =
    Weather(
        main?.temp?.roundToInt() ?: 0,
        weather?.get(0)?.icon?.let { toWeatherCondition(it) } ?: WeatherCondition.DEFAULT,
        weather?.get(0)?.icon?.let { makeIconUrl(it) } ?: "",
        weather?.get(0)?.description?.capitalizeWords() ?: "",
        main?.humidity?.toInt() ?: 0,
        wind?.speed ?: 0.0,
        wind?.deg?.toInt()?.let { WindDirection.degreeToDirection(it) } ?: WindDirection.NORTH,
        main?.pressure?.toInt() ?: 0
    )

private fun toWeatherCondition(icon: String) = when (icon.substring(0, 2).toInt()) {
    1 -> WeatherCondition.CLEAR_SKY
    2 -> WeatherCondition.FEW_CLOUDS
    3 -> WeatherCondition.SCATTERED_CLOUDS
    4 -> WeatherCondition.BROKEN_CLOUDS
    9 -> WeatherCondition.SHOWER_RAIN
    10 -> WeatherCondition.RAIN
    11 -> WeatherCondition.THUNDERSTORM
    13 -> WeatherCondition.SNOW
    50 -> WeatherCondition.MIST
    else -> WeatherCondition.DEFAULT
}

private fun makeIconUrl(iconId: String) = BuildConfig.ICON_URL + "$iconId@2x.png"

// Превращает все буквы в слове в заглавные
private fun String.capitalizeWords()
        : String =
    split(" ").joinToString(" ") { it.capitalize(Locale.getDefault()) }