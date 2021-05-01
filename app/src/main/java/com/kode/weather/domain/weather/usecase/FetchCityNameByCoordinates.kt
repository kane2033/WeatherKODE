package com.kode.weather.domain.weather.usecase

import com.kode.weather.domain.base.usecase.UseCase
import com.kode.weather.domain.weather.datasource.GeoCoderDataSource
import com.kode.weather.domain.weather.entity.LocationCoordinates
import com.kode.weather.domain.weather.exception.CityNotFound

class FetchCityNameByCoordinates(private val geoCoderDataSource: GeoCoderDataSource) :
    UseCase<String, LocationCoordinates>() {
    override fun run(param: LocationCoordinates): String {
        return geoCoderDataSource.getCityNameByCoordinates(param) ?: throw CityNotFound
    }
}