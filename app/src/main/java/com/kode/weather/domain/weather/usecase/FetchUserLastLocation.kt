package com.kode.weather.domain.weather.usecase

import com.kode.weather.domain.base.usecase.UseCase
import com.kode.weather.domain.weather.datasource.LastLocationDataSource
import com.kode.weather.domain.weather.entity.LocationCoordinates
import com.kode.weather.domain.weather.exception.LastLocationNotAvailable
import com.kode.weather.domain.weather.exception.LocationPermissionMissing

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