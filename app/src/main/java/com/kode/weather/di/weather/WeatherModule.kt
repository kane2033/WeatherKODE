package com.kode.weather.di.weather

import com.kode.weather.data.weather.datasource.GeoCoderDataSourceImpl
import com.kode.weather.data.weather.datasource.LastLocationDataSourceImpl
import com.kode.weather.domain.weather.datasource.GeoCoderDataSource
import com.kode.weather.domain.weather.datasource.LastLocationDataSource
import com.kode.weather.domain.weather.usecase.FetchCityNameByCoordinates
import com.kode.weather.domain.weather.usecase.FetchUserLastLocation
import com.kode.weather.presentation.map.MapViewModel
import com.kode.weather.presentation.weather.WeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object WeatherModule {
    val module = module {
        single<LastLocationDataSource> { LastLocationDataSourceImpl(androidContext()) }
        single { FetchUserLastLocation(get()) }

        single<GeoCoderDataSource> { GeoCoderDataSourceImpl(androidContext()) }
        single { FetchCityNameByCoordinates(get()) }

        viewModel { MapViewModel(get(), get()) }

        viewModel { parameters -> WeatherViewModel(cityName = parameters.get()) }
    }
}