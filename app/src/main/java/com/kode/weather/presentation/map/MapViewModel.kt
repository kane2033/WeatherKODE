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

    private val _cityName = MutableLiveData<String>()
    val cityName = _cityName.asLiveData()

    private val _cityCoordinates = MutableLiveData<LocationCoordinates>()
    val cityCoordinates = _cityCoordinates.asLiveData()

    fun setCityCoordinates(latitude: Double, longitude: Double) {
        _cityCoordinates.value = LocationCoordinates(latitude, longitude)
        fetchCityNameByCoordinates(latitude, longitude)
    }

    private val _cityDialogVisibility = MutableLiveData<Boolean>()
    val cityDialogVisibility = _cityDialogVisibility.asLiveData()

    fun fetchUserLastLocation() {
        viewModelScope.launch {
            val result = fetchUserLastLocation(Unit).loadingIndication().single()
            result.fold(
                onSuccess = { _lastLocation.value = Event(it) },
                onFailure = { handleFailure(it) }
            )
        }
    }

    fun fetchCityNameByCoordinates(latitude: Double, longitude: Double) {
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