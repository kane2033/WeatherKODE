package com.kode.weather.domain.map.usecase

import com.kode.weather.domain.base.usecase.UseCase
import com.kode.weather.domain.map.datasource.LastLocationDataSource
import com.kode.weather.domain.map.entity.LocationCoordinates
import com.kode.weather.domain.map.exception.LastLocationNotAvailable
import com.kode.weather.domain.map.exception.LocationPermissionMissing

class FetchUserLastLocation(private val lastLocationDataSource: LastLocationDataSource) :
    UseCase<LocationCoordinates, Unit>() {
    override fun run(param: Unit): LocationCoordinates {
        try {
            return lastLocationDataSource.getLastLocation() ?: throw LastLocationNotAvailable
        } catch (e: SecurityException) {
            throw LocationPermissionMissing
        }
    }
}