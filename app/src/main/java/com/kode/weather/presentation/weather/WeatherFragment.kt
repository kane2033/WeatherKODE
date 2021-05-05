package com.kode.weather.presentation.weather

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.weather.R
import com.kode.weather.databinding.FragmentWeatherBinding
import com.kode.weather.domain.base.exception.info.FullScreenFailureInfo
import com.kode.weather.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class WeatherFragment : BaseFragment(R.layout.fragment_weather) {

    private val args: WeatherFragmentArgs by navArgs()

    override val viewModel: WeatherViewModel by viewModel { parametersOf(args.cityName) }

    private val binding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@WeatherFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        val cityNotFoundFailureInfo = FullScreenFailureInfo(
            retryClickedCallback = { findNavController().popBackStack(R.id.weatherFragment, true) },
            title = getString(R.string.error_city_not_found_title),
            text = getString(R.string.error_city_not_found),
            buttonText = getString(R.string.error_city_not_found_button)
        )

        handleFailure(
            // При любой необработанной ошибке пытаемся снова запросить погоду города
            // (в том числе и при проблемах с интернетом)
            baseRetryClickedCallback = viewModel::fetchCityWeather,
            handleRequestFailure = { code ->
                when (code) {
                    404 -> cityNotFoundFailureInfo
                    else -> null
                }
            })
    }
}