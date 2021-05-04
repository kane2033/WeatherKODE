package com.kode.weather.presentation.map

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.weather.entity.LocationCoordinates
import com.kode.weather.domain.weather.usecase.FetchCityNameByCoordinates
import com.kode.weather.domain.weather.usecase.FetchUserLastLocation
import com.kode.weather.presentation.base.BaseViewModel
import com.kode.weather.presentation.map.entity.SingleCircleMarker
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class MapViewModel(
    private val fetchUserLastLocation: FetchUserLastLocation,
    private val fetchCityName: FetchCityNameByCoordinates
) : BaseViewModel() {

    // Отображаемое название города
    private val _cityName = MutableLiveData<String>()
    val cityName = _cityName.asLiveData()

    // Фрагмент наблюдает за выбранным названием,
    // при изменении открывается след. фрагмент
    private val _selectedCityName = MutableLiveData<Event<String>>()
    val selectedCityName = _selectedCityName.asLiveData()

    private val _cityCoordinates = MutableLiveData<LocationCoordinates>()
    val cityCoordinates = _cityCoordinates.asLiveData()

    // Т.к. для получение местоположения юзера нужен пермишн,
    // fetchUserLastLocation вызывается в onViewCreated фрагмента после проверки пермишна,
    // что происходит каждый раз при возврате из бекстека, вследствие чего возникают баги,
    // поэтому вьюмодель хранит, было ли запрошено местоположение юзера.
    private var hasUserLastLocation = false

    private val _cityDialogVisibility = MutableLiveData(false)
    val cityDialogVisibility = _cityDialogVisibility.asLiveData()

    // Единичный маркер с радиусом на гугл карте
    private var mapMarker: SingleCircleMarker? = null

    fun setupMarker(radius: Double, color: Int, @DrawableRes id: Int, context: Context?) {
        // Создание маркера, только если он еще не создан
        if (mapMarker == null) {
            mapMarker = SingleCircleMarker(radius, color).apply { setIcon(id, context) }
        }
    }

    fun placeMarker(googleMap: GoogleMap, coordinates: LatLng) {
        mapMarker?.createCircleMarker(googleMap, coordinates)
    }

    fun setCityCoordinates(latitude: Double, longitude: Double) {
        _cityCoordinates.value = LocationCoordinates(latitude, longitude)
        fetchCityNameByCoordinates(latitude, longitude)
    }

    fun showWeatherClick() {
        _cityName.value?.let { _selectedCityName.value = Event(it) }
    }

    fun closeDialogClick() {
        _cityDialogVisibility.value = false
    }

    fun fetchUserLastLocation() {
        if (hasUserLastLocation) return

        viewModelScope.launch {
            val result = fetchUserLastLocation(Unit).loadingIndication().single()
            result.fold(
                onSuccess = {
                    hasUserLastLocation = true
                    setCityCoordinates(it.latitude, it.longitude)
                },
                onFailure = { handleFailure(it) }
            )
        }
    }

    private fun fetchCityNameByCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val cityCoordinates = LocationCoordinates(latitude, longitude)
            fetchCityName(cityCoordinates)
                .loadingIndication()
                .single()
                .fold(
                    onSuccess = {
                        _cityName.value = it
                        _cityDialogVisibility.value = true
                    },
                    onFailure = {
                        _cityDialogVisibility.value = false
                    }
                )

        }
    }
}