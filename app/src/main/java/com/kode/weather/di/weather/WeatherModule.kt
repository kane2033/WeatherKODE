package com.kode.weather.di.weather

import com.kode.weather.data.weather.datasource.network.WeatherDataSourceImpl
import com.kode.weather.domain.weather.datasource.WeatherDataSource
import com.kode.weather.domain.weather.usecase.FetchCityWeather
import com.kode.weather.presentation.weather.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object WeatherModule {
    val module = module {
        single<WeatherDataSource> { WeatherDataSourceImpl(get()) }
        single { FetchCityWeather(get()) }

        viewModel { WeatherViewModel(get(), get()) }
    }
}