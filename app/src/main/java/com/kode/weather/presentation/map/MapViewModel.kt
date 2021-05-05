package com.kode.weather.presentation.map

import android.Manifest
import android.app.Application
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.kode.weather.R
import com.kode.weather.domain.base.Event
import com.kode.weather.domain.weather.entity.LocationCoordinates
import com.kode.weather.domain.weather.exception.LocationPermissionMissing
import com.kode.weather.domain.weather.usecase.FetchCityNameByCoordinates
import com.kode.weather.domain.weather.usecase.FetchUserLastLocation
import com.kode.weather.presentation.base.viewmodel.BaseViewModel
import com.kode.weather.presentation.map.entity.SingleCircleMarker
import com.kode.weather.presentation.map.extention.checkPermission
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

class MapViewModel(
    private val requestLocationPermission: ActivityResultLauncher<String>,
    private val fetchUserLastLocation: FetchUserLastLocation,
    private val fetchCityName: FetchCityNameByCoordinates,
    application: Application,
    baseViewModel: BaseViewModel,
) : BaseViewModel by baseViewModel, AndroidViewModel(application) {

    // AndroidViewModel используется,
    // чтобы при запросе последней локации юзера
    // можно было проверить пермишн.
    // Если делать это onViewCreated фрагмента, это приводит к багам.

    companion object {
        private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val RADIUS_DEFAULT = 2000.0 // 2 КМ
    }

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
    private val mapMarker = SingleCircleMarker(
        RADIUS_DEFAULT,
        ContextCompat.getColor(application.applicationContext, R.color.blue_circle),
        R.drawable.ic_marker,
        application.applicationContext
    )

    fun placeMarker(googleMap: GoogleMap, coordinates: LatLng) {
        mapMarker.createCircleMarker(googleMap, coordinates)
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

    init {
        fetchUserLastLocationWithPermissionCheck()
    }

    // Запрос последней локации юзера с проверкой пермишна
    fun fetchUserLastLocationWithPermissionCheck() {
        if (getApplication<Application>().applicationContext.checkPermission(LOCATION_PERMISSION)) {
            fetchUserLastLocation()
        } else {
            requestLocationPermission.launch(LOCATION_PERMISSION)
        }
    }

    // Даем знать, что пермишн локации не получен
    fun setPermissionFailure() {
        handleFailure(LocationPermissionMissing)
    }

    private fun fetchUserLastLocation() {
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