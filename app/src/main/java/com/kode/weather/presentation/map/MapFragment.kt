package com.kode.weather.presentation.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kode.weather.R
import com.kode.weather.databinding.FragmentMapBinding
import com.kode.weather.domain.base.exception.info.SmallFailureInfo
import com.kode.weather.domain.weather.exception.LastLocationNotAvailable
import com.kode.weather.domain.weather.exception.LocationPermissionMissing
import com.kode.weather.presentation.base.BaseFragment
import com.kode.weather.presentation.map.extention.permissionActivityResultContract
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class MapFragment : BaseFragment(R.layout.fragment_map) {

    companion object {
        private const val ZOOM_DEFAULT = 11F
    }

    // Запрос пермишна на локацию через Activity Result API
    private val requestLocationPermission = permissionActivityResultContract(
        onGranted = { viewModel.fetchUserLastLocationWithPermissionCheck() },
        onRejected = { viewModel.setPermissionFailure() }
    )

    override val viewModel: MapViewModel by viewModel { parametersOf(requestLocationPermission) }

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

        // Обработка ошибок, присущих данному фрагменту
        handleFailure(handleFailure = { failure ->
            when (failure) {
                // Не удалось получить локацию юзера - можно повторить
                is LastLocationNotAvailable -> SmallFailureInfo(
                    viewModel::fetchUserLastLocationWithPermissionCheck,
                    getString(R.string.error_last_location)
                )
                // Юзер не дал пермишн на локацию - можно еще раз запросить
                is LocationPermissionMissing -> SmallFailureInfo(
                    viewModel::fetchUserLastLocationWithPermissionCheck,
                    getString(R.string.error_permission_location)
                )
                else -> null
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
    }
}