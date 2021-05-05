package com.kode.weather.di.weather

import androidx.activity.result.ActivityResultLauncher
import com.kode.weather.data.weather.datasource.location.GeoCoderDataSourceImpl
import com.kode.weather.data.weather.datasource.location.LastLocationDataSourceImpl
import com.kode.weather.data.weather.datasource.network.WeatherDataSourceImpl
import com.kode.weather.domain.weather.datasource.GeoCoderDataSource
import com.kode.weather.domain.weather.datasource.LastLocationDataSource
import com.kode.weather.domain.weather.datasource.WeatherDataSource
import com.kode.weather.domain.weather.usecase.FetchCityNameByCoordinates
import com.kode.weather.domain.weather.usecase.FetchCityWeather
import com.kode.weather.domain.weather.usecase.FetchUserLastLocation
import com.kode.weather.presentation.map.MapViewModel
import com.kode.weather.presentation.weather.WeatherViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object WeatherModule {
    val module = module {
        single<LastLocationDataSource> { LastLocationDataSourceImpl(androidContext()) }
        single { FetchUserLastLocation(get()) }

        single<GeoCoderDataSource> { GeoCoderDataSourceImpl(androidContext()) }
        single { FetchCityNameByCoordinates(get()) }

        viewModel { (requestPermission: ActivityResultLauncher<String>) ->
            MapViewModel(requestPermission, get(), get(), androidApplication(), get())
        }

        single<WeatherDataSource> { WeatherDataSourceImpl(get(), get()) }
        single { FetchCityWeather(get()) }

        viewModel { WeatherViewModel(get(), get(), get()) }
    }
}