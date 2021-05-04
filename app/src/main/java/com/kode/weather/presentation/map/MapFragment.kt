package com.kode.weather.presentation.map

import android.Manifest
import android.os.Bundle
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
import com.kode.weather.domain.weather.exception.LastLocationNotAvailable
import com.kode.weather.domain.weather.exception.LocationPermissionMissing
import com.kode.weather.presentation.base.BaseFragment
import com.kode.weather.presentation.map.extention.checkPermission
import com.kode.weather.presentation.map.extention.permissionActivityResultContract
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MapFragment : BaseFragment(R.layout.fragment_map) {

    companion object {
        private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val ZOOM_DEFAULT = 11F
        private const val RADIUS_DEFAULT = 2000.0 // 5 КМ
    }

    override val viewModel: MapViewModel by viewModel()

    private val binding: FragmentMapBinding by viewBinding(FragmentMapBinding::bind)

    // Получаем из фрагмента контейнера карту-фрагмент
    private fun Fragment.getMapFragment() =
        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment


    // Запрос пермишна на локацию через Activity Result API
    private val requestLocationPermission = permissionActivityResultContract(
        onGranted = { viewModel.fetchUserLastLocation() },
        onRejected = { makeToast(R.string.error_permission_location) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@MapFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        handleFailure({
            when (it) {
                is LastLocationNotAvailable -> makeToast(R.string.error_last_location)
                is LocationPermissionMissing -> makeToast(R.string.error_permission_location)
                else -> makeToast(R.string.error_base_title)
            }
        })

/*        mapMarker = SingleCircleMarker(
            RADIUS_DEFAULT, ContextCompat.getColor(requireContext(), R.color.blue_circle)
        )
        mapMarker.setIcon(R.drawable.ic_marker, context)*/

        viewModel.setupMarker(
            RADIUS_DEFAULT,
            ContextCompat.getColor(requireContext(), R.color.blue_circle),
            R.drawable.ic_marker,
            context
        )

        // Работа с картой будет выполнена только если есть пермишн на локацию
        if (requireContext().checkPermission(LOCATION_PERMISSION)) {
            viewModel.fetchUserLastLocation()
        } else {
            requestLocationPermission.launch(LOCATION_PERMISSION)
        }

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