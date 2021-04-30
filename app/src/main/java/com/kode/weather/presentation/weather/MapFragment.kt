package com.kode.weather.presentation.weather

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.kode.weather.R
import com.kode.weather.databinding.FragmentMapBinding
import com.kode.weather.presentation.base.BaseFragment
import com.kode.weather.presentation.weather.extention.checkPermission
import com.kode.weather.presentation.weather.extention.permissionActivityResultContract
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseFragment(R.layout.fragment_map) {

    companion object {
        private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val ZOOM_DEFAULT = 10F
    }

    override val viewModel: WeatherViewModel by viewModel()

    private val binding: FragmentMapBinding by viewBinding(FragmentMapBinding::bind)

    // Отображает текущую локацию
    private var fusedLocationClient: FusedLocationProviderClient? = null

    // Перед выполнением функции, проверяется пермишн
    @SuppressLint("MissingPermission")
    private val onLocationGranted: () -> Unit = {

        // Получаем из фрагмента контейнера карту-фрагмент
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // Первоначальные настройки
        mapFragment.getMapAsync { googleMap ->
            googleMap.isMyLocationEnabled = true
        }

        // Инициализация клиента, предоставляющего текущую локацию
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Зум на стартовую локацию
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            if (location == null) {
                //viewModel._failure.value = LastLocationNotAvailable
                makeToast(R.string.error_last_location)
                return@addOnSuccessListener
            }

            mapFragment.getMapAsync { googleMap ->
                val lastLocationCoordinates = LatLng(location.latitude, location.longitude)
                val latLngZoom =
                    CameraUpdateFactory.newLatLngZoom(lastLocationCoordinates, ZOOM_DEFAULT)
                googleMap.animateCamera(latLngZoom)
            }
        }
    }

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
        handleFailure()

        // Работа с картой будет выполнена только если есть пермишн на локацию
        if (requireContext().checkPermission(LOCATION_PERMISSION)) {
            onLocationGranted()
        } else {
            requestLocationPermission.launch(LOCATION_PERMISSION)
        }
    }
}