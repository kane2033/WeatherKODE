package com.kode.weather.data.weather.datasource.network.api

import com.kode.weather.data.weather.datasource.network.model.WeatherByCityDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("weather")
    fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("units") units: String
    ): Call<WeatherByCityDto?>
}