package com.kode.weather.domain.weather.datasource

import com.kode.weather.domain.weather.entity.LocationCoordinates

interface LastLocationDataSource {
    // Система требует доступ к геолокации,
    // при его отсутствии бросается SecurityException
    @Throws(SecurityException::class)
    fun getLastLocation(): LocationCoordinates?
}