package com.kode.weather.di.map

import com.kode.weather.data.map.datasource.location.GeoCoderDataSourceImpl
import com.kode.weather.data.map.datasource.location.LastLocationDataSourceImpl
import com.kode.weather.domain.map.datasource.GeoCoderDataSource
import com.kode.weather.domain.map.datasource.LastLocationDataSource
import com.kode.weather.domain.map.usecase.FetchCityNameByCoordinates
import com.kode.weather.domain.map.usecase.FetchUserLastLocation
import com.kode.weather.presentation.map.MapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MapModule {
    val module = module {
        single<LastLocationDataSource> { LastLocationDataSourceImpl(androidContext()) }
        single { FetchUserLastLocation(get()) }

        single<GeoCoderDataSource> { GeoCoderDataSourceImpl(androidContext()) }
        single { FetchCityNameByCoordinates(get()) }

        viewModel { MapViewModel(get(), get()) }
    }
}