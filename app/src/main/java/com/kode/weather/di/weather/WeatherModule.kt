package com.kode.weather.di.weather

import com.kode.weather.data.base.network.NetworkAvailabilityInterceptor
import com.kode.weather.data.weather.datasource.network.WeatherDataSourceImpl
import com.kode.weather.data.weather.datasource.network.interceptor.OpenWeatherAuthInterceptor
import com.kode.weather.di.base.NetworkModule
import com.kode.weather.domain.weather.datasource.WeatherDataSource
import com.kode.weather.domain.weather.usecase.FetchCityWeather
import com.kode.weather.presentation.weather.WeatherViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object WeatherModule {

    const val RETROFIT_AUTH = "auth"
    private const val CLIENT_AUTH = "client_auth"

    val module = module {
        single(named(CLIENT_AUTH)) { provideOkHttpClientWithAuth(get()) }
        single(named(RETROFIT_AUTH)) { NetworkModule.provideRetrofit(get(named(CLIENT_AUTH))) }

        single<WeatherDataSource> { WeatherDataSourceImpl() }
        single { FetchCityWeather(get()) }

        viewModel { WeatherViewModel(get(), get()) }
    }

    private fun provideOkHttpClientWithAuth(interceptor: NetworkAvailabilityInterceptor): OkHttpClient {
        val builder = NetworkModule.provideOkHttpClient(interceptor).newBuilder()
        return builder.addInterceptor(OpenWeatherAuthInterceptor()).build()
    }
}