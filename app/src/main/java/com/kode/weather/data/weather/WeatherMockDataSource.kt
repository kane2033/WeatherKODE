package com.kode.weather.data.weather

import com.kode.weather.domain.base.exception.Failure
import com.kode.weather.domain.weather.entity.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class WeatherMockDataSource() {

    // Не используем Result
    fun getWeatherInfo(): WeatherInfo {
        throw Failure.NetworkConnection
        //return WeatherInfo("Weather info!")
        //return if (Random.nextBoolean()) WeatherInfo("info") else throw Exception()
    }
}