package com.kode.weather.presentation.weather

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.kode.weather.R
import com.kode.weather.domain.weather.entity.WeatherCondition

object WeatherBindingAdapters {

    @JvmStatic
    @BindingAdapter(value = ["weatherImage"])
    fun ImageView.setImageFromWeatherCondition(weatherCondition: WeatherCondition?) {
        if (weatherCondition == null) return

        val imageResource = when (weatherCondition) {
            WeatherCondition.CLEAR_SKY -> R.drawable.weather_clear_sky
            WeatherCondition.FEW_CLOUDS -> R.drawable.weather_few_clouds
            WeatherCondition.SCATTERED_CLOUDS -> R.drawable.weather_scattered_clouds
            WeatherCondition.BROKEN_CLOUDS -> R.drawable.weather_broken_clouds
            WeatherCondition.SHOWER_RAIN -> R.drawable.weather_shower_rain
            WeatherCondition.RAIN -> R.drawable.weather_rain
            WeatherCondition.THUNDERSTORM -> R.drawable.weather_thunderstorm
            WeatherCondition.SNOW -> R.drawable.weather_snow
            WeatherCondition.MIST -> R.drawable.weather_mist
            WeatherCondition.DEFAULT -> R.drawable.weather_clear_sky
        }
        setImageResource(imageResource)
    }
}