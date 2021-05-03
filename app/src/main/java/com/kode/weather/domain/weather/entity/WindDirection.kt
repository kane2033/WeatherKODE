package com.kode.weather.domain.weather.entity

import com.kode.weather.domain.base.exception.Failure

enum class WindDirection(val direction: String, private val degree: Int) {
    EAST("E", 90),
    NORTH_EAST("NE", 45),
    NORTH("N", 0),
    NORTH_WEST("NW", 315),
    WEST("W", 270),
    SOUTH_WEST("SW", 225),
    SOUTH("S", 180),
    SOUTH_EAST("SE", 135);

    companion object {

        private const val delta = 23

        fun degreeToDirection(degree: Int): WindDirection {
            return values().find {
                val directionRange = it.degree - delta..it.degree + delta
                degree in directionRange
            } ?: isNorth(degree)
        }

        // Костыль, потому что имеет дело с градусами,
        // а в текущей реализации цикл выше пропустит промежуток от 315 + delta .. 360
        private fun isNorth(degree: Int) =
            if (degree in NORTH_WEST.degree + delta..NORTH.degree) {
                NORTH
            } else {
                throw IncorrectWindDirection
            }
    }

    object IncorrectWindDirection : Failure.FeatureFailure()
}