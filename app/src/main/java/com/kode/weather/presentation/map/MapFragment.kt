package com.kode.weather.presentation.map

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kode.weather.R
import com.kode.weather.databinding.FragmentMapBinding
import com.kode.weather.domain.base.exception.info.SmallFailureInfo
import com.kode.weather.domain.map.exception.LastLocationNotAvailable
import com.kode.weather.domain.map.exception.LocationPermissionMissing
import com.kode.weather.presentation.base.*
import com.kode.weather.presentation.base.exception.RetryClickedInterface
import com.kode.weather.presentation.map.entity.SingleCircleMarker
import com.kode.weather.presentation.map.extention.checkPermission
import com.kode.weather.presentation.map.extention.permissionActivityResultContract
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class MapFragment : Fragment(R.layout.fragment_map), RetryClickedInterface {

    companion object {
        private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val RADIUS_DEFAULT = 2000.0 // 2 КМ
        private const val ZOOM_DEFAULT = 11F
    }

    // Запрос пермишна на локацию через Activity Result API
    private val requestLocationPermission = permissionActivityResultContract(
        onGranted = { viewModel.setHasLocationPermission(true) },
        onRejected = { viewModel.setHasLocationPermission(false) }
    )

    private val viewModel: MapViewModel by viewModel { parametersOf(requestLocationPermission) }

    private val binding: FragmentMapBinding by viewBinding(FragmentMapBinding::bind)

    // Получаем из фрагмента контейнера карту-фрагмент
    private fun Fragment.getMapFragment() =
        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@MapFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        // Единичный маркер на карте
        viewModel.initMarker(
            SingleCircleMarker(
                RADIUS_DEFAULT,
                ContextCompat.getColor(requireContext(), R.color.blue_circle),
                R.drawable.ic_marker,
                requireContext()
            )
        )

        // Есть ли пермишн на геолокацию?
        if (requireContext().checkPermission(LOCATION_PERMISSION)) {
            // Даем знать вьюмодели
            viewModel.setHasLocationPermission(true)
        } else {
            // Запрос пермишна
            requestLocationPermission.launch(LOCATION_PERMISSION)
        }

        // Обработка результата запроса пермишна через requestLocationPermission
        viewModel.hasLocationPermission.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { hasPermission ->
                if (hasPermission) {
                    viewModel.fetchUserLastLocation()
                } else {
                    viewModel.setPermissionFailure()
                }
            }
        })

        val mapFragment = getMapFragment()

        // При клике на любую точку карты
        mapFragment.getMapAsync { googleMap ->
            googleMap.setOnMapClickListener { coordinates ->
                viewModel.setCityCoordinates(coordinates.latitude, coordinates.longitude)
            }
        }

        viewModel.cityCoordinates.observe(viewLifecycleOwner, {
            mapFragment.getMapAsync { googleMap ->
                val coordinates = LatLng(it.latitude, it.longitude)
                viewModel.placeMarker(googleMap, coordinates)
                // Не уменьшаем зум, если текущий выше дефолтного
                val cameraPosition = if (googleMap.cameraPosition.zoom > ZOOM_DEFAULT) {
                    CameraUpdateFactory.newLatLng(coordinates)
                } else {
                    CameraUpdateFactory.newLatLngZoom(coordinates, ZOOM_DEFAULT)
                }
                // Камера перемещается на созданную точку
                googleMap.animateCamera(cameraPosition)
            }
        })

        viewModel.selectedCityName.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { cityName ->
                val action = MapFragmentDirections.actionMapFragmentToWeatherFragment(cityName)
                findNavController().navigate(action)
            }
        })

        setHasOptionsMenu(true)

        viewModel.uiState.observeEvent(viewLifecycleOwner, {
            if (it is UiState.Failure) {
                openFailureView(failure = it.failure, getFailureInfo = { failure ->
                    when (failure) {
                        is LastLocationNotAvailable -> SmallFailureInfo(
                            ::onRetryClicked,
                            getString(R.string.error_last_location)
                        )
                        is LocationPermissionMissing -> SmallFailureInfo(
                            ::onRetryClicked,
                            getString(R.string.error_permission_location)
                        )
                        else -> null
                    }
                })
            }
        })
    }

    // Действие при нажатии кнопки "retry/refresh" у окна, отображающего ошибку
    override fun onRetryClicked() {
        val failureState = viewModel.uiState.value?.peekContent() as? UiState.Failure
        when (failureState?.failure) {
            is LastLocationNotAvailable -> viewModel.fetchUserLastLocation()
            is LocationPermissionMissing -> requestLocationPermission.launch(LOCATION_PERMISSION)
            else -> {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.searchButton) {
            makeToast(R.string.in_development)
        }
        return super.onOptionsItemSelected(item)
    }
}