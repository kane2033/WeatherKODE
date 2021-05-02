package com.kode.weather.data.weather.datasource.location

import android.content.Context
import android.location.Geocoder
import com.kode.weather.domain.weather.datasource.GeoCoderDataSource
import com.kode.weather.domain.weather.entity.LocationCoordinates
import java.util.*

class GeoCoderDataSourceImpl(context: Context) : GeoCoderDataSource {

    private val geoCoder = Geocoder(context, Locale.getDefault())

    override fun getCityNameByCoordinates(coordinates: LocationCoordinates): String? {
        val locations = geoCoder.getFromLocation(coordinates.latitude, coordinates.longitude, 5)
        return locations.find { it.locality != null }?.locality
    }
}