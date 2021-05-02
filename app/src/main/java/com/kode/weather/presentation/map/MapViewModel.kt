package com.kode.weather.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.weather.entity.LocationCoordinates
import com.kode.weather.domain.weather.usecase.FetchCityNameByCoordinates
import com.kode.weather.domain.weather.usecase.FetchUserLastLocation
import com.kode.weather.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class MapViewModel(
    private val fetchUserLastLocation: FetchUserLastLocation,
    private val fetchCityName: FetchCityNameByCoordinates
) : BaseViewModel() {

    private val _lastLocation = MutableLiveData<Event<LocationCoordinates>>()
    val lastLocation = _lastLocation.asLiveData()

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
        viewModelScope.launch {
            val result = fetchUserLastLocation(Unit).loadingIndication().single()
            result.fold(
                onSuccess = { setCityCoordinates(it.latitude, it.longitude) },
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