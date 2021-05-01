package com.kode.weather.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.weather.entity.LocationCoordinates
import com.kode.weather.domain.weather.usecase.FetchUserLastLocation
import com.kode.weather.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class MapViewModel(private val fetchUserLastLocation: FetchUserLastLocation) : BaseViewModel() {


    private val _lastLocation = MutableLiveData<Event<LocationCoordinates>>()
    val lastLocation: LiveData<Event<LocationCoordinates>> = _lastLocation

    init {
        //fetchUserLastLocation()
    }

    fun fetchUserLastLocation() {
        viewModelScope.launch {
            val result = fetchUserLastLocation(Unit).loadingIndication().single()
            result.fold(
                onSuccess = { _lastLocation.value = Event(it) },
                onFailure = { handleFailure(it) }
            )
        }
    }
}