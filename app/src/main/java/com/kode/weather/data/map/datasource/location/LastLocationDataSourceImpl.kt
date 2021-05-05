package com.kode.weather.data.map.datasource.location

import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import com.kode.weather.domain.map.datasource.LastLocationDataSource
import com.kode.weather.domain.map.entity.LocationCoordinates

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