package com.kode.weather

import android.app.Application
import com.kode.weather.di.base.BaseModule
import com.kode.weather.di.base.NetworkModule
import com.kode.weather.di.map.MapModule
import com.kode.weather.di.weather.WeatherModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@AndroidApplication)
            modules(
                BaseModule.module,
                NetworkModule.module,
                MapModule.module,
                WeatherModule.module
            )
        }
    }
}