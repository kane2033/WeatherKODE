package com.kode.weather.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.map.entity.LocationCoordinates
import com.kode.weather.domain.map.exception.LocationPermissionMissing
import com.kode.weather.domain.map.usecase.FetchCityNameByCoordinates
import com.kode.weather.domain.map.usecase.FetchUserLastLocation
import com.kode.weather.presentation.base.UiState
import com.kode.weather.presentation.base.asLiveData
import com.kode.weather.presentation.base.handleFailure
import com.kode.weather.presentation.base.loadingIndication
import com.kode.weather.presentation.map.entity.SingleCircleMarker
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class MapViewModel(
    private val fetchUserLastLocation: FetchUserLastLocation,
    private val fetchCityName: FetchCityNameByCoordinates
) : ViewModel() {

    private val _uiState = MutableLiveData<Event<UiState>>()
    val uiState = _uiState.asLiveData()

    // Хранит наличие пермишна геолокации,
    // необходимое для получения последней локации юзера
    private val _hasLocationPermission = MutableLiveData<Event<Boolean>>()
    val hasLocationPermission = _hasLocationPermission.asLiveData()

    // Отображаемое название города
    private val _cityName = MutableLiveData<String>()
    val cityName = _cityName.asLiveData()

    // Фрагмент наблюдает за выбранным названием,
    // при изменении открывается след. фрагмент
    private val _selectedCityName = MutableLiveData<Event<String>>()
    val selectedCityName = _selectedCityName.asLiveData()

    private val _cityCoordinates = MutableLiveData<LocationCoordinates>()
    val cityCoordinates = _cityCoordinates.asLiveData()

    private val _cityDialogVisibility = MutableLiveData(false)
    val cityDialogVisibility = _cityDialogVisibility.asLiveData()

    // Единичный маркер с радиусом на гугл карте
    private var mapMarker: SingleCircleMarker? = null

    // Инициализация маркера на карте,
    // только если он еще не создан (null)
    fun initMarker(marker: SingleCircleMarker) {
        if (mapMarker == null) {
            mapMarker = marker
        }
    }

    fun placeMarker(googleMap: GoogleMap, coordinates: LatLng) {
        mapMarker?.createCircleMarker(googleMap, coordinates)
    }

    // Вызывается при получении пермишна
    fun setHasLocationPermission(hasPermission: Boolean) {
        // Пермишн должен быть запрошен только один раз за экран
        if (_hasLocationPermission.value?.peekContent() != true) {
            _hasLocationPermission.value = Event(hasPermission)
        }
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

    // Даем знать, что пермишн локации не получен
    fun setPermissionFailure() {
        LocationPermissionMissing.handleFailure(_uiState)
    }

    fun fetchUserLastLocation() {
        // Запрашиваем локацию юзера, только если есть пермишн
        if (hasLocationPermission.value?.peekContent() == false) return

        viewModelScope.launch {
            val result = fetchUserLastLocation(Unit).loadingIndication(_uiState).single()
            result.fold(
                onSuccess = { setCityCoordinates(it.latitude, it.longitude) },
                onFailure = { it.handleFailure(_uiState) }
            )
        }
    }

    private fun fetchCityNameByCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val cityCoordinates = LocationCoordinates(latitude, longitude)
            val result = fetchCityName(cityCoordinates).loadingIndication(_uiState).single()
            result.fold(
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