package com.kode.weather.data.weather.datasource

import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import com.kode.weather.domain.weather.datasource.LastLocationDataSource
import com.kode.weather.domain.weather.entity.LocationCoordinates

class LastLocationDataSourceImpl(context: Context) : LastLocationDataSource {

    // Получение клиента локации через контекст приложения
    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // lastLocation требует пермишн локации
    @RequiresPermission(
        anyOf = ["android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"]
    )
    override fun getLastLocation(): LocationCoordinates? {
        // Получаем локацию синхронно. Локация может быть не получена, тогда возвращается null.
        val location = Tasks.await(fusedLocationProviderClient.lastLocation) ?: return null
        // Возвращаем только координаты
        return LocationCoordinates(location.latitude, location.longitude)
    }
}