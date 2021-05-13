package com.kode.weather.data.weather.datasource.network

import com.kode.weather.data.base.network.extention.getBodySafely
import com.kode.weather.data.weather.datasource.network.api.OpenWeatherApi
import com.kode.weather.data.weather.datasource.network.converter.toWeather
import com.kode.weather.domain.base.exception.Failure
import com.kode.weather.domain.weather.datasource.WeatherDataSource
import com.kode.weather.domain.weather.entity.Weather
import com.kode.weather.domain.weather.entity.WeatherQuery
import com.kode.weather.domain.weather.exception.FetchWeatherFailure
import retrofit2.Retrofit
import java.net.HttpURLConnection

class WeatherDataSourceImpl(retrofit: Retrofit) : WeatherDataSource {

    private val api: OpenWeatherApi by lazy { retrofit.create(OpenWeatherApi::class.java) }

    override fun getWeatherByCityName(query: WeatherQuery): Weather {
        val response =
            api.getWeatherByCityName(query.cityName, query.tempMeasurement.toString()).execute()
        return response.getBodySafely(
            transform = { it.toWeather() },
            requestFailure = { requestFailureToDomainFailure(it) }
        )
    }

    // Обработка http ошибок по кодам и возврат доменных ошибок
    private fun requestFailureToDomainFailure(requestFailure: Failure.RequestFailure): Failure {
        return when (requestFailure.code) {
            HttpURLConnection.HTTP_NOT_FOUND -> FetchWeatherFailure.NotFound
            else -> requestFailure
        }
    }
}