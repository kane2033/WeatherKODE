package com.kode.weather.domain.weather.usecase

import android.util.Log
import com.kode.weather.data.weather.WeatherMockDataSource
import com.kode.weather.domain.base.exception.Failure
import com.kode.weather.domain.base.usecase.UseCase
import com.kode.weather.domain.weather.datasource.WeatherDataSource
import com.kode.weather.domain.weather.entity.WeatherInfo
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*

class FetchWeatherInfo(private val weatherDataSource: WeatherDataSource):
    UseCase<WeatherInfo, Unit>() {

    override fun run(param: Unit): Flow<Result<WeatherInfo>> {
        return flow {
            val weatherInfo = try {
                kotlinx.coroutines.delay(5000L)
                Result.success(weatherDataSource.getWeatherInfo())
            } catch (failure: Failure) {
                Result.failure(failure)
            }
            emit(weatherInfo)
        }
    }
}