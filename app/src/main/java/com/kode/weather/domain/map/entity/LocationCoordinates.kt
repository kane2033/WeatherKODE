package com.kode.weather.domain.map.entity

import java.util.*
import kotlin.math.abs

data class LocationCoordinates(val latitude: Double, val longitude: Double) {

    val degrees: String = toDMS()

    // Перевод в формат DMS:
    // 45°16'44.7"N 9°43'33.2"E
    private fun toDMS(): String {
        val latitudeDirection = if (latitude >= 0) "N" else "S"
        val latitudeDMS = coordinateToDMS(latitude, latitudeDirection)

        val longitudeDirection = if (longitude >= 0) "E" else "W"
        val longitudeDMS = coordinateToDMS(longitude, longitudeDirection)

        return "$latitudeDMS $longitudeDMS"
    }

    private fun coordinateToDMS(coordinate: Double, direction: String): String {
        val degrees = abs(coordinate)
        val minutes = ((degrees - degrees.toInt()) * 60)
        val seconds = ((degrees - degrees.toInt()) * 3600 % 60)
        return "${degrees.toInt()}°${minutes.toInt()}′${seconds.format(1)}″$direction"
    }

    private fun Double.format(roundTo: Int) =
        String.format(Locale.getDefault(), "%.${roundTo}f", this)
}