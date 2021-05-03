package com.kode.weather

import android.app.Application
import com.kode.weather.di.base.NetworkModule
import com.kode.weather.di.weather.WeatherModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AndroidApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidLogger()
            androidContext(this@AndroidApplication)
            modules(WeatherModule.module)
            modules(NetworkModule.module)
        }
    }
}