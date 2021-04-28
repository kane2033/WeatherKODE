package com.kode.weather.di.weather

import com.kode.weather.data.weather.WeatherMockDataSource
import com.kode.weather.domain.weather.datasource.WeatherDataSource
import com.kode.weather.domain.weather.usecase.FetchWeatherInfo
import com.kode.weather.presentation.weather.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object WeatherModule {
    val module = module {
        single<WeatherDataSource> { WeatherMockDataSource() }
        single { FetchWeatherInfo(get()) }
        viewModel { WeatherViewModel(get()) }
    }
}