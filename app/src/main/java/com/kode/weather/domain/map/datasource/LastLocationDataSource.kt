package com.kode.weather.domain.map.datasource

import com.kode.weather.domain.map.entity.LocationCoordinates

interface LastLocationDataSource {
    // Система требует доступ к геолокации,
    // при его отсутствии бросается SecurityException
    @Throws(SecurityException::class)
    fun getLastLocation(): LocationCoordinates?
}