package com.kode.weather.presentation.map

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kode.weather.R
import com.kode.weather.databinding.FragmentMapBinding
import com.kode.weather.domain.weather.exception.LastLocationNotAvailable
import com.kode.weather.domain.weather.exception.LocationPermissionMissing
import com.kode.weather.presentation.base.BaseFragment
import com.kode.weather.presentation.map.entity.SingleCircleMarker
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

    // Единичный маркер с радиусом на гугл карте
    private lateinit var mapMarker: SingleCircleMarker

    // Перед выполнением функции проверяется пермишн
    private val onLocationGranted = {
        // Запрашиваем местоположение юзера
        viewModel.fetchUserLastLocation()

        // Зум на локацию юзера по получению локации
        viewModel.lastLocation.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                getMapFragment().getMapAsync { googleMap ->
                    // Запрашиваем название города, в котором находится юзер
                    viewModel.fetchCityNameByCoordinates(it.latitude, it.longitude)

                    // Отображение местоположения юзера маркером и перемещением камеры
                    val lastLocationCoordinates = LatLng(it.latitude, it.longitude)
                    mapMarker.createCircleMarker(googleMap, lastLocationCoordinates)
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(lastLocationCoordinates, ZOOM_DEFAULT)
                    )
                }
            }
        })
    }

    // Запрос пермишна на локацию через Activity Result API
    private val requestLocationPermission = permissionActivityResultContract(
        onGranted = onLocationGranted,
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

        mapMarker = SingleCircleMarker(
            RADIUS_DEFAULT, ContextCompat.getColor(requireContext(), R.color.blue_circle)
        )
        mapMarker.setIcon(R.drawable.ic_marker, context)

        // Работа с картой будет выполнена только если есть пермишн на локацию
        if (requireContext().checkPermission(LOCATION_PERMISSION)) {
            onLocationGranted()
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
                mapMarker.createCircleMarker(googleMap, coordinates)
                // Не увеличиваем зум, если текущий выше дефолтного
                val cameraPosition = if (googleMap.cameraPosition.zoom > ZOOM_DEFAULT) {
                    CameraUpdateFactory.newLatLng(coordinates)
                } else {
                    CameraUpdateFactory.newLatLngZoom(coordinates, ZOOM_DEFAULT)
                }
                // Камера перемещается на созданную точку
                googleMap.animateCamera(cameraPosition)
            }
        })

        viewModel.cityName.observe(viewLifecycleOwner, {
            makeToast("Clicked city: $it")
        })

        viewModel.cityDialogVisibility.observe(viewLifecycleOwner, { isVisible ->
            if (!isVisible) makeToast("City not found!")
        })
    }
}