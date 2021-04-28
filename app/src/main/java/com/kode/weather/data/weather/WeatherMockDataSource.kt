package com.kode.weather.data.weather

import com.kode.weather.domain.base.exception.Failure
import com.kode.weather.domain.weather.datasource.WeatherDataSource
import com.kode.weather.domain.weather.entity.WeatherInfo
import com.kode.weather.domain.weather.exception.FetchWeatherException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class WeatherMockDataSource: WeatherDataSource {

    // Не используем Result
    override fun getWeatherInfo(): WeatherInfo {
        return if (Random.nextBoolean()) WeatherInfo("info") else throw FetchWeatherException
    }
}