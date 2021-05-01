package com.kode.weather.presentation.map.entity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

/**
 * Маркер на карте с кастомной иконкой в центре и кругом с опредленным радиусом.
 * Данный маркер при пересоздании автоматически убирается с карты.
 * Таким образом, на карте может быть только один такой маркер.
 * */
class SingleCircleMarker(radius: Double, fillColor: Int = Color.BLUE) {
    private var marker: Marker? = null
    private var circle: Circle? = null

    // Иконку нужно установить позже, чтобы избежать
    // "BitmapDescriptorFactory is not initialized"
    private var icon: BitmapDescriptor? = null

    private val markerOptions =
        MarkerOptions().flat(true).anchor(0.5f, 0.5f)

    private val circleOptions =
        CircleOptions().radius(radius).strokeWidth(0f)
            .fillColor(fillColor)

    // Создание маркера на googleMap по указанным координатам и удаление
    // существующего маркера
    fun createCircleMarker(googleMap: GoogleMap, coordinates: LatLng) {
        marker?.remove()
        circle?.remove()

        marker = googleMap.addMarker(markerOptions.position(coordinates).icon(icon))
        circle = googleMap.addCircle(circleOptions.center(coordinates))
    }

    fun setIcon(@DrawableRes resId: Int, context: Context?) {
        icon = iconFromVectorDrawable(resId, context)
    }

    // Drawable -> BitmapDescriptor convert
    private fun iconFromVectorDrawable(
        @DrawableRes resId: Int,
        context: Context?
    ): BitmapDescriptor {
        val defaultMarker = BitmapDescriptorFactory.defaultMarker()
        if (context == null) return defaultMarker
        val drawable = ContextCompat.getDrawable(context, resId) ?: return defaultMarker

        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}