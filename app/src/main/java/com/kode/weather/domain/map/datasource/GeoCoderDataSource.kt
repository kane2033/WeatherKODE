package com.kode.weather.domain.map.datasource

import com.kode.weather.domain.map.entity.LocationCoordinates

interface GeoCoderDataSource {
    fun getCityNameByCoordinates(coordinates: LocationCoordinates): String?
}