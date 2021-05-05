package com.kode.weather.domain.map.usecase

import com.kode.weather.domain.base.usecase.UseCase
import com.kode.weather.domain.map.datasource.GeoCoderDataSource
import com.kode.weather.domain.map.entity.LocationCoordinates
import com.kode.weather.domain.map.exception.CityNotFound

class FetchCityNameByCoordinates(private val geoCoderDataSource: GeoCoderDataSource) :
    UseCase<String, LocationCoordinates>() {
    override fun run(param: LocationCoordinates): String {
        return geoCoderDataSource.getCityNameByCoordinates(param) ?: throw CityNotFound
    }
}