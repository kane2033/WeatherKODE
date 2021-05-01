package com.kode.weather.domain.weather.datasource

import com.kode.weather.domain.weather.entity.LocationCoordinates

interface GeoCoderDataSource {
    fun getCityNameByCoordinates(coordinates: LocationCoordinates): String?
}